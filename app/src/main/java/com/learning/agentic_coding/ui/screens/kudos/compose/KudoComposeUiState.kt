package com.learning.agentic_coding.ui.screens.kudos.compose

import com.learning.agentic_coding.domain.KudosHashtag
import com.learning.agentic_coding.domain.KudosRecipient
import com.learning.agentic_coding.ui.screens.kudos.compose.components.FormatSpan
import com.learning.agentic_coding.ui.screens.kudos.compose.components.SpanType

/**
 * UI state for the Send Kudos screen (MoMorph PV7jBVZU1N + variants). Form fields are
 * plain primitives; lookup catalogs and submit lifecycle wrap the network bits.
 */
data class KudoComposeUiState(
    val recipients: List<KudosRecipient> = emptyList(),
    val hashtags: List<KudosHashtag> = emptyList(),
    val selectedRecipient: KudosRecipient? = null,
    val recipientQuery: String = "",
    val title: String = "",
    val body: String = "",
    /** Rich-text styling held in the VM so it survives leaving/returning to the screen. */
    val bodySpans: List<FormatSpan> = emptyList(),
    /** Sticky toolbar toggles (Bold/Italic/Strikethrough armed for the typing caret). */
    val pendingFormats: Set<SpanType> = emptySet(),
    val selectedHashtags: List<KudosHashtag> = emptyList(),
    val imageUris: List<String> = emptyList(),
    val imageBytes: List<ByteArray> = emptyList(),
    val isAnonymous: Boolean = false,
    val anonNickname: String = "",
    val showError: Boolean = false,
    val submitState: SubmitState = SubmitState.Idle,
    /** Email of the signed-in user, used to block sending a kudo to yourself. */
    val currentUserEmail: String? = null,
) {
    /** Maximum images per Kudo (matches "+ Image (Tối đa 5)" label). */
    val maxImages: Int get() = MAX_IMAGES
    /** Maximum hashtags per Kudo (matches "+ Hashtag (Tối đa 5)" label). */
    val maxHashtags: Int get() = MAX_HASHTAGS
    /** Spec D / B.4: title ≤ 100 chars, message ≤ 1000 chars. */
    val maxTitleLength: Int get() = MAX_TITLE_LENGTH
    val maxBodyLength: Int get() = MAX_BODY_LENGTH

    /** True when the chosen recipient is the signed-in user (spec B.2: cannot kudo yourself). */
    val isSelfRecipient: Boolean
        get() {
            val email = selectedRecipient?.email ?: return false
            return currentUserEmail != null && email.equals(currentUserEmail, ignoreCase = true)
        }

    fun isValid(): Boolean {
        if (selectedRecipient == null || isSelfRecipient) return false
        if (title.isBlank()) return false
        if (body.isBlank()) return false
        if (selectedHashtags.isEmpty()) return false
        if (isAnonymous && anonNickname.isBlank()) return false
        return true
    }

    val filteredRecipients: List<KudosRecipient>
        get() = if (recipientQuery.isBlank()) recipients
        else recipients.filter { it.name.contains(recipientQuery, ignoreCase = true) }

    companion object {
        const val MAX_IMAGES = 5
        const val MAX_HASHTAGS = 5
        const val MAX_TITLE_LENGTH = 100
        const val MAX_BODY_LENGTH = 1000
    }
}

sealed interface SubmitState {
    data object Idle : SubmitState
    data object Submitting : SubmitState
    data class Success(val kudoId: String) : SubmitState
    data class Error(val message: String) : SubmitState
}
