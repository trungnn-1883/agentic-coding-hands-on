package com.learning.agentic_coding.ui.screens.kudos.secretbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.kudos.KudosRepository
import com.learning.agentic_coding.data.kudos.KudosResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Drives the Open Secret Box flow:
 *  - load → snapshot unopened count from `kudos_user_stats`
 *  - tap box → repository.openSecretBox() → reveal reward, decrement counter
 *  - tap reveal → back to idle
 */
class OpenBoxViewModel(private val kudosRepository: KudosRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<OpenBoxUiState>(OpenBoxUiState.Loading)
    val uiState: StateFlow<OpenBoxUiState> = _uiState.asStateFlow()

    init {
        loadInitialCount()
    }

    private fun loadInitialCount() {
        viewModelScope.launch {
            val home = kudosRepository.observeHome().firstOrNull { it !is KudosResult.Loading }
            _uiState.value = when (home) {
                is KudosResult.Success -> OpenBoxUiState.Idle(home.stats.secretBoxUnopened)
                is KudosResult.Error -> OpenBoxUiState.Failure(home.message)
                else -> OpenBoxUiState.Failure("Unable to load Secret Box state")
            }
        }
    }

    fun onBoxTap() {
        val current = _uiState.value
        if (current !is OpenBoxUiState.Idle || current.unopenedCount <= 0) return
        _uiState.value = OpenBoxUiState.Opening(current.unopenedCount)
        viewModelScope.launch {
            val result = kudosRepository.openSecretBox()
            _uiState.value = result.fold(
                onSuccess = { outcome ->
                    val reward = outcome.reward
                    if (reward == null) {
                        OpenBoxUiState.Idle(outcome.unopenedRemaining)
                    } else {
                        OpenBoxUiState.Revealed(reward, outcome.unopenedRemaining)
                    }
                },
                onFailure = { error ->
                    OpenBoxUiState.Failure(error.message ?: "Failed to open Secret Box")
                },
            )
        }
    }

    /** From [OpenBoxUiState.Revealed] back to [OpenBoxUiState.Idle] (tap anywhere). */
    fun onDismissReveal() {
        val current = _uiState.value
        if (current is OpenBoxUiState.Revealed) {
            _uiState.value = OpenBoxUiState.Idle(current.unopenedCount)
        }
    }

    fun retry() {
        if (_uiState.value is OpenBoxUiState.Failure) {
            _uiState.value = OpenBoxUiState.Loading
            loadInitialCount()
        }
    }
}
