package com.learning.agentic_coding.ui.screens.kudos.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.kudos.KudosRepository
import com.learning.agentic_coding.data.kudos.RecentSearchStore
import com.learning.agentic_coding.domain.KudosRecipient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Drives the Sunner search screen. Debounces query input (300ms) before hitting
 * [KudosRepository.searchRecipients]; observes persisted recents from [RecentSearchStore].
 * Selecting a result records it as a recent (navigation handled by the host).
 */
class SunnerSearchViewModel(
    private val kudosRepository: KudosRepository,
    private val recentSearchStore: RecentSearchStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SunnerSearchUiState())
    val uiState: StateFlow<SunnerSearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            recentSearchStore.recentsFlow.collect { recents ->
                _uiState.update { it.copy(recents = recents) }
            }
        }
    }

    fun onQueryChange(value: String) {
        // Spec caps the search field at 100 chars.
        val capped = value.take(100)
        _uiState.update { it.copy(query = capped) }
        searchJob?.cancel()
        if (capped.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), searching = false) }
            return
        }
        _uiState.update { it.copy(searching = true, results = emptyList()) }
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_MS)
            val results = kudosRepository.searchRecipients(capped)
            _uiState.update { it.copy(results = results, searching = false) }
        }
    }

    /** Records the picked Sunner as a recent search. Host handles navigation (stubbed). */
    fun onResultSelected(recipient: KudosRecipient) {
        viewModelScope.launch { recentSearchStore.add(recipient) }
    }

    fun onRemoveRecent(id: String) {
        viewModelScope.launch { recentSearchStore.remove(id) }
    }

    fun onToggleShowAllRecents() {
        _uiState.update { it.copy(showAllRecents = !it.showAllRecents) }
    }

    private companion object {
        const val DEBOUNCE_MS = 300L
    }
}
