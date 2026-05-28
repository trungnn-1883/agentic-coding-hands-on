package com.learning.agentic_coding.ui.screens.kudos.main

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

/**
 * Drives the Kudos home screen: combines locale + KudosResult fetch + dropdown selections.
 * Re-fetch is triggered through [retry] (similar to HomeViewModel.retryAwards).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class KudosHomeViewModel(
    private val kudosRepository: KudosRepository,
    private val localeRepository: LocaleRepository,
) : ViewModel() {

    private val refreshTicker = MutableStateFlow(0)
    private val departmentFilter = MutableStateFlow<String?>(null)
    private val hashtagFilter = MutableStateFlow<String?>(null)

    private val dataFlow = refreshTicker.flatMapLatest { kudosRepository.observeHome() }

    val uiState: StateFlow<KudosHomeUiState> = combine(
        localeRepository.languageFlow,
        dataFlow,
        departmentFilter,
        hashtagFilter,
    ) { language, data, dept, hashtag ->
        KudosHomeUiState(language = language, data = data, departmentFilter = dept, hashtagFilter = hashtag)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = KudosHomeUiState(),
    )

    fun selectDepartment(dept: String?) {
        departmentFilter.value = dept
    }

    fun selectHashtag(hashtag: String?) {
        hashtagFilter.value = hashtag
    }

    fun retry() {
        if (uiState.value.data is KudosResult.Error) refreshTicker.value++
    }

    /** Unconditional refetch — used when stats change off-screen (e.g. after Open Secret Box). */
    fun refresh() {
        refreshTicker.value++
    }
}
