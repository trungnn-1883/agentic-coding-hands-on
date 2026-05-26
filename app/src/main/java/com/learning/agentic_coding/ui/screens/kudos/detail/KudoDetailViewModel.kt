package com.learning.agentic_coding.ui.screens.kudos.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.kudos.KudoDetailResult
import com.learning.agentic_coding.data.kudos.KudosRepository
import com.learning.agentic_coding.data.locale.LocaleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/** Drives the Kudo detail screen — single observable fetch, local heart toggle, retry. */
@OptIn(ExperimentalCoroutinesApi::class)
class KudoDetailViewModel(
    kudosRepository: KudosRepository,
    localeRepository: LocaleRepository,
    private val kudoId: String,
) : ViewModel() {

    private val refreshTicker = MutableStateFlow(0)
    private val localState = MutableStateFlow(LocalActions())
    private val dataFlow = refreshTicker.flatMapLatest { kudosRepository.observeKudoDetail(kudoId) }

    val uiState: StateFlow<KudoDetailUiState> = combine(
        localeRepository.languageFlow,
        dataFlow,
        localState,
    ) { language, data, actions ->
        KudoDetailUiState(
            language = language,
            data = data,
            heartDelta = actions.heartDelta,
            isLiked = actions.isLiked,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = KudoDetailUiState(),
    )

    fun retry() {
        if (uiState.value.data is KudoDetailResult.Error) refreshTicker.value++
    }

    fun toggleHeart() {
        localState.update {
            if (it.isLiked) it.copy(isLiked = false, heartDelta = it.heartDelta - 1)
            else it.copy(isLiked = true, heartDelta = it.heartDelta + 1)
        }
    }

    private data class LocalActions(val isLiked: Boolean = false, val heartDelta: Int = 0)
}
