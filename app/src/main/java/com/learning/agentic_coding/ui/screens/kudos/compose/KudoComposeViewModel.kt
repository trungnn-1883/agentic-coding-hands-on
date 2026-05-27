package com.learning.agentic_coding.ui.screens.kudos.compose

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.auth.AuthRepository
import com.learning.agentic_coding.data.kudos.KudosPostInsert
import com.learning.agentic_coding.data.kudos.KudosRepository
import com.learning.agentic_coding.domain.KudosBadge
import com.learning.agentic_coding.domain.KudosHashtag
import com.learning.agentic_coding.domain.KudosRecipient
import com.learning.agentic_coding.ui.screens.kudos.compose.components.FormatSpan
import com.learning.agentic_coding.ui.screens.kudos.compose.components.SpanType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * State holder for the Send-Kudos form. Loads recipient + hashtag catalogs on init,
 * resolves the current signed-in user as the sender via email lookup in kudos_recipients,
 * and submits via [KudosRepository.createKudo].
 *
 * Image bytes are read **at pick-time** (on IO) into [KudoComposeUiState.imageBytes]; this
 * avoids both main-thread I/O at submit and the photo-picker URI expiry that would
 * silently drop attachments after a process death.
 */
class KudoComposeViewModel(
    private val kudosRepository: KudosRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(KudoComposeUiState())
    val uiState: StateFlow<KudoComposeUiState> = _uiState.asStateFlow()

    private var senderRecipient: KudosRecipient? = null

    init {
        viewModelScope.launch {
            val recipients = kudosRepository.listRecipients()
            val hashtags = kudosRepository.listHashtags()
            val email = authRepository.currentUser.first()?.email
            _uiState.update { it.copy(recipients = recipients, hashtags = hashtags, currentUserEmail = email) }
            if (!email.isNullOrBlank()) {
                senderRecipient = kudosRepository.findRecipientByEmail(email)
            }
        }
    }

    fun onRecipientSelect(recipient: KudosRecipient) {
        _uiState.update { it.copy(selectedRecipient = recipient, recipientQuery = recipient.name) }
    }

    fun onRecipientQuery(query: String) {
        _uiState.update {
            val keepSelected = it.selectedRecipient?.name == query
            it.copy(recipientQuery = query, selectedRecipient = if (keepSelected) it.selectedRecipient else null)
        }
    }

    fun onTitleChange(value: String) =
        _uiState.update { it.copy(title = value.take(it.maxTitleLength)) }
    fun onBodyChange(value: String) =
        _uiState.update { it.copy(body = value.take(it.maxBodyLength)) }

    /** Persist the editor's styled ranges so formatting survives navigation away and back. */
    fun onBodySpansChange(spans: List<FormatSpan>) = _uiState.update { it.copy(bodySpans = spans) }

    /** Persist the sticky toolbar toggles for the same reason. */
    fun onPendingFormatsChange(pending: Set<SpanType>) =
        _uiState.update { it.copy(pendingFormats = pending) }
    fun onAnonymousToggle() = _uiState.update { it.copy(isAnonymous = !it.isAnonymous) }
    fun onAnonNicknameChange(value: String) = _uiState.update { it.copy(anonNickname = value) }

    fun onHashtagToggle(tag: KudosHashtag) {
        _uiState.update { state ->
            val current = state.selectedHashtags
            val next = if (current.any { it.id == tag.id }) current.filterNot { it.id == tag.id }
            else if (current.size >= state.maxHashtags) current
            else current + tag
            state.copy(selectedHashtags = next)
        }
    }

    fun onHashtagRemove(tag: KudosHashtag) {
        _uiState.update { it.copy(selectedHashtags = it.selectedHashtags.filterNot { t -> t.id == tag.id }) }
    }

    /**
     * Reads bytes for each picked URI on Dispatchers.IO so the temporary photo-picker
     * grants are consumed before they expire. Results are stored alongside the URI list.
     */
    fun onImagesPicked(contentResolver: ContentResolver, uris: List<Uri>) {
        viewModelScope.launch {
            val (uriList, byteList) = withContext(Dispatchers.IO) {
                val current = _uiState.value
                val pairs = uris.mapNotNull { uri ->
                    runCatching {
                        contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    }.getOrNull()?.let { uri.toString() to it }
                }
                // Dedup on (uri, bytes) pairs so bytes stay aligned with their URI even when an
                // already-picked URI is re-selected (H1: keep mergedBytes index-matched to mergedUris).
                val deduped = (current.imageUris.zip(current.imageBytes) + pairs)
                    .distinctBy { it.first }
                    .take(current.maxImages)
                deduped.map { it.first } to deduped.map { it.second }
            }
            _uiState.update { it.copy(imageUris = uriList, imageBytes = byteList) }
        }
    }

    fun onImageRemove(uri: String) {
        _uiState.update { state ->
            val idx = state.imageUris.indexOf(uri)
            if (idx < 0) state
            else state.copy(
                imageUris = state.imageUris.toMutableList().apply { removeAt(idx) },
                imageBytes = state.imageBytes.toMutableList().apply { if (idx < size) removeAt(idx) },
            )
        }
    }

    /**
     * Clears all form fields after a successful send so re-opening the compose screen starts
     * blank. Keeps the loaded recipient/hashtag catalogs and the current-user email so the
     * screen stays usable without re-fetching.
     */
    fun reset() {
        _uiState.update {
            KudoComposeUiState(
                recipients = it.recipients,
                hashtags = it.hashtags,
                currentUserEmail = it.currentUserEmail,
            )
        }
    }

    fun onSubmit() {
        val state = _uiState.value
        if (!state.isValid()) {
            _uiState.update { it.copy(showError = true) }
            return
        }
        _uiState.update { it.copy(submitState = SubmitState.Submitting, showError = false) }
        viewModelScope.launch {
            val user = authRepository.currentUser.first()
            val sender = senderRecipient ?: defaultSenderFromUser(user?.displayName ?: user?.email.orEmpty())
            val receiver = state.selectedRecipient!!
            val payload = KudosPostInsert(
                senderName = sender.name,
                senderDept = sender.dept,
                senderBadge = sender.badge.wire,
                senderAvatarUrl = sender.avatarUrl,
                receiverName = receiver.name,
                receiverDept = receiver.dept,
                receiverBadge = receiver.badge.wire,
                receiverAvatarUrl = receiver.avatarUrl,
                title = state.title.trim(),
                content = state.body,
                hashtags = state.selectedHashtags.map { it.tag },
                isAnonymous = state.isAnonymous,
                anonNickname = if (state.isAnonymous) state.anonNickname else null,
            )
            val result = kudosRepository.createKudo(payload, state.imageBytes)
            _uiState.update {
                it.copy(
                    submitState = result.fold(
                        onSuccess = { id -> SubmitState.Success(id) },
                        onFailure = { exp -> SubmitState.Error("Không gửi được Kudo. Vui lòng thử lại. ${exp}") },
                    ),
                )
            }
        }
    }

    private fun defaultSenderFromUser(displayName: String): KudosRecipient = KudosRecipient(
        id = "current-user",
        email = null,
        name = displayName.ifBlank { "Bạn" },
        dept = "CECV1",
        badge = KudosBadge.RisingHero,
        avatarUrl = null,
    )
}
