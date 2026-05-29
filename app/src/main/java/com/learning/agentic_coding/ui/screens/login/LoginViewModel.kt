package com.learning.agentic_coding.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.agentic_coding.data.auth.AuthRepository
import com.learning.agentic_coding.data.auth.AuthRepositoryException
import com.learning.agentic_coding.data.locale.LocaleRepository
import com.learning.agentic_coding.domain.AuthError
import com.learning.agentic_coding.domain.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val localeRepository: LocaleRepository,
    private val mockSignIn: Boolean = true, // Set to true to mock 2s delay instead of actual sign-in
) : ViewModel() {

    private val localState = MutableStateFlow(LocalState())

    val uiState: StateFlow<LoginUiState> = combine(
        localeRepository.languageFlow,
        localState,
    ) { language, local ->
        LoginUiState(language = language, isLoading = local.isLoading, error = local.error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = LoginUiState(),
    )

    fun onLanguageSelect(language: Language) {
        viewModelScope.launch { localeRepository.setLanguage(language) }
    }

    /**
     * Initiates Google sign-in. Re-entrant calls while a sign-in is in flight are dropped
     * (TC_LOGIN_FUN_008 — double-click prevention).
     */
    fun onLoginClick(context: Context) {
        if (localState.value.isLoading) return
        localState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                if (mockSignIn) {
                    // Mock mode: show real Google account chooser but skip Supabase sign-in
                    authRepository.signInWithGoogleMock(context)
                } else {
                    authRepository.signInWithGoogle(context)
                }
                localState.update { it.copy(isLoading = false, error = null) }
            } catch (e: AuthRepositoryException) {
                localState.update { it.copy(isLoading = false, error = e.authError) }
            } catch (e: Exception) {
                localState.update { it.copy(isLoading = false, error = AuthError.Unknown(e.message)) }
            }
        }
    }

    fun onErrorDismissed() {
        localState.update { it.copy(error = null) }
    }

    private data class LocalState(
        val isLoading: Boolean = false,
        val error: AuthError? = null,
    )

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
