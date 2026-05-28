package com.learning.agentic_coding.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.locale.LocaleRepository
import com.learning.agentic_coding.data.notifications.NotificationsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Drives the Notifications screen: combines locale + notifications fetch. Read-state
 * mutations re-tick the refresh flow so list + bell badge stay in sync.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsViewModel(
    private val repository: NotificationsRepository,
    localeRepository: LocaleRepository,
) : ViewModel() {

    private val refreshTicker = MutableStateFlow(0)

    private val dataFlow = refreshTicker.flatMapLatest { repository.observeAll() }

    val uiState: StateFlow<NotificationsUiState> = combine(
        localeRepository.languageFlow,
        dataFlow,
    ) { language, data ->
        NotificationsUiState(language = language, data = data)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = NotificationsUiState(),
    )

    fun markAsRead(id: String) {
        viewModelScope.launch {
            repository.markAsRead(id)
            refreshTicker.value++
        }
    }

    fun markAllRead() {
        viewModelScope.launch {
            repository.markAllRead()
            refreshTicker.value++
        }
    }

    fun retry() {
        refreshTicker.value++
    }
}
