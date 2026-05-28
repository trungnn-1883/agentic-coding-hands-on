package com.learning.agentic_coding.ui.screens.award

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.awards.AwardsRepository
import com.learning.agentic_coding.data.awards.AwardsResult
import com.learning.agentic_coding.data.locale.LocaleRepository
import com.learning.agentic_coding.data.notifications.NotificationsRepository
import com.learning.agentic_coding.domain.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Drives the Award Detail screen. Awards list comes from [awardsRepository.observe];
 * the user-selected slug + dropdown open flag are kept in memory and combined.
 * Retry re-triggers the awards flow via [refreshTicker].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AwardDetailViewModel(
    private val awardsRepository: AwardsRepository,
    private val localeRepository: LocaleRepository,
    private val notificationsRepository: NotificationsRepository,
    initialSlug: String,
) : ViewModel() {

    private val selectedSlug = MutableStateFlow(initialSlug)
    private val dropdownOpen = MutableStateFlow(false)
    private val refreshTicker = MutableStateFlow(0)

    private val awardsFlow = refreshTicker.flatMapLatest { awardsRepository.observe() }

    val uiState: StateFlow<AwardDetailUiState> = combine(
        awardsFlow,
        selectedSlug,
        localeRepository.languageFlow,
        dropdownOpen,
        notificationsRepository.unreadCount,
    ) { awards, slug, language, open, unread ->
        AwardDetailUiState(
            awards = awards,
            selectedSlug = slug,
            language = language,
            isDropdownOpen = open,
            notificationUnread = unread,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AwardDetailUiState(selectedSlug = initialSlug),
    )

    fun selectAward(slug: String) {
        selectedSlug.value = slug
        dropdownOpen.value = false
    }

    fun setDropdownOpen(open: Boolean) {
        dropdownOpen.value = open
    }

    fun onLanguageSelect(language: Language) {
        viewModelScope.launch { localeRepository.setLanguage(language) }
    }

    fun retry() {
        if (uiState.value.awards is AwardsResult.Error) {
            refreshTicker.value++
        }
    }
}
