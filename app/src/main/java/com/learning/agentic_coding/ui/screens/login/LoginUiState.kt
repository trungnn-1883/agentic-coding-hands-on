package com.learning.agentic_coding.ui.screens.login

import androidx.annotation.StringRes
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.AuthError
import com.learning.agentic_coding.domain.Language

data class LoginUiState(
    val language: Language = Language.DEFAULT,
    val isLoading: Boolean = false,
    val error: AuthError? = null,
)

@StringRes
internal fun AuthError.toMessageRes(): Int = when (this) {
    AuthError.Cancelled -> R.string.error_auth_cancelled
    AuthError.NoGoogleAccount -> R.string.error_auth_no_google_account
    AuthError.Network -> R.string.error_auth_network
    is AuthError.Unknown -> R.string.error_auth_generic
}
