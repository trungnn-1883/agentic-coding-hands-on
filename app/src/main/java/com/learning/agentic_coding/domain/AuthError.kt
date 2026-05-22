package com.learning.agentic_coding.domain

/**
 * Categorized auth failures. UI maps each to a localized string resource.
 *
 * Drives test cases TC_LOGIN_FUN_010 (network/cancel), TC_LOGIN_FUN_015 (invalid account).
 */
sealed class AuthError {
    object Cancelled : AuthError()
    object NoGoogleAccount : AuthError()
    object Network : AuthError()
    data class Unknown(val message: String?) : AuthError()
}
