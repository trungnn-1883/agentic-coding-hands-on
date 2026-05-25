package com.learning.agentic_coding.ui.screens.kudos.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.kudos.KudosRepository
import com.learning.agentic_coding.data.kudos.KudosResult
import com.learning.agentic_coding.data.locale.LocaleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

/** Drives the All Kudos detail screen — single observable fetch with retry. */
@OptIn(ExperimentalCoroutinesApi::class)
class AllKudosViewModel(
    kudosRepository: KudosRepository,
    localeRepository: LocaleRepository,
) : ViewModel() {

    private val refreshTicker = MutableStateFlow(0)
    private val dataFlow = refreshTicker.flatMapLatest { kudosRepository.observeHome() }

    val uiState: StateFlow<AllKudosUiState> = combine(
        localeRepository.languageFlow,
        dataFlow,
    ) { language, data ->
        AllKudosUiState(language = language, data = data)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AllKudosUiState(),
    )

    fun retry() {
        if (uiState.value.data is KudosResult.Error) refreshTicker.value++
    }
}
