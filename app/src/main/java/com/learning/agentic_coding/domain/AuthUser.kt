package com.learning.agentic_coding.domain

/** Minimal user representation surfaced to the UI layer. */
data class AuthUser(
    val id: String,
    val email: String,
    val displayName: String?,
)
