package com.learning.agentic_coding.ui.screens.login

import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.AuthError
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUiStateTest {

    @Test
    fun `NoGoogleAccount maps to its dedicated string`() {
        assertEquals(R.string.error_auth_no_google_account, AuthError.NoGoogleAccount.toMessageRes())
    }

    @Test
    fun `all AuthError variants resolve to distinct string resources`() {
        val ids = listOf(
            AuthError.Cancelled.toMessageRes(),
            AuthError.NoGoogleAccount.toMessageRes(),
            AuthError.Network.toMessageRes(),
            AuthError.Unknown(null).toMessageRes(),
        )
        assertEquals(ids.size, ids.toSet().size)
    }
}
