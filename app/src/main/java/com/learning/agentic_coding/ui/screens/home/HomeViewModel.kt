package com.learning.agentic_coding.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.auth.AuthRepository
import com.learning.agentic_coding.data.locale.LocaleRepository
import com.learning.agentic_coding.domain.AuthUser
import com.learning.agentic_coding.domain.Language
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val localeRepository: LocaleRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        authRepository.currentUser,
        localeRepository.languageFlow,
    ) { user, language ->
        HomeUiState(user = user, language = language)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState(),
    )

    fun onLanguageSelect(language: Language) {
        viewModelScope.launch { localeRepository.setLanguage(language) }
    }

    fun onLogout() {
        viewModelScope.launch { authRepository.signOut() }
    }
}

data class HomeUiState(
    val user: AuthUser? = null,
    val language: Language = Language.DEFAULT,
)
