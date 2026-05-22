package com.learning.agentic_coding.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.auth.AuthRepository
import com.learning.agentic_coding.data.awards.AwardsRepository
import com.learning.agentic_coding.data.awards.AwardsResult
import com.learning.agentic_coding.data.kudos.KudosRepository
import com.learning.agentic_coding.data.locale.LocaleRepository
import com.learning.agentic_coding.domain.Language
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Drives Home: combines auth, locale, awards, kudos flag, and a per-minute countdown
 * ticker. Awards fetch is re-triggered via [retryAwards] by bumping a refresh counter
 * that `flatMapLatest` watches.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val authRepository: AuthRepository,
    private val localeRepository: LocaleRepository,
    private val awardsRepository: AwardsRepository,
    private val kudosRepository: KudosRepository,
    private val now: () -> LocalDateTime = { LocalDateTime.now(EVENT_ZONE) },
) : ViewModel() {

    private val refreshTicker = MutableStateFlow(0)

    private val awardsFlow = refreshTicker.flatMapLatest { awardsRepository.observe() }

    private val countdownFlow = flow {
        while (true) {
            emit(computeCountdown(now()))
            delay(COUNTDOWN_TICK_MS)
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        authRepository.currentUser,
        localeRepository.languageFlow,
        awardsFlow,
        countdownFlow,
    ) { user, language, awards, countdown ->
        HomeUiState(
            user = user,
            language = language,
            awards = awards,
            countdown = countdown,
            isKudosAvailable = kudosRepository.isKudosAvailable,
            notificationUnread = HARDCODED_UNREAD,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState(isKudosAvailable = kudosRepository.isKudosAvailable),
    )

    fun onLanguageSelect(language: Language) {
        viewModelScope.launch { localeRepository.setLanguage(language) }
    }

    fun retryAwards() {
        if (uiState.value.awards is AwardsResult.Error) {
            refreshTicker.value++
        }
    }

    companion object {
        private val EVENT_TARGET: LocalDateTime = LocalDateTime.of(2026, 6, 26, 0, 0)
        private val EVENT_ZONE: ZoneId = ZoneId.of("Asia/Saigon")
        private const val COUNTDOWN_TICK_MS = 60_000L
        private const val HARDCODED_UNREAD = 1

        internal fun computeCountdown(now: LocalDateTime): CountdownState {
            val remaining = Duration.between(now, EVENT_TARGET)
            if (remaining.isNegative || remaining.isZero) {
                return CountdownState(days = 0, hours = 0, minutes = 0, isEnded = true)
            }
            val totalMinutes = remaining.toMinutes()
            return CountdownState(
                days = (totalMinutes / (60L * 24L)).toInt(),
                hours = ((totalMinutes / 60L) % 24L).toInt(),
                minutes = (totalMinutes % 60L).toInt(),
                isEnded = false,
            )
        }
    }
}
