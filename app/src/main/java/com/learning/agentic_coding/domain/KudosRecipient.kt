package com.learning.agentic_coding.domain

/**
 * Selectable recipient surfaced in the "Người nhận" dropdown (MoMorph 5MU728Tjck).
 * Also used to resolve the current signed-in user's dept/badge/avatar by email match.
 */
data class KudosRecipient(
    val id: String,
    val email: String?,
    val name: String,
    val dept: String,
    val badge: KudosBadge,
    val avatarUrl: String?,
)

/** Selectable hashtag from the "+ Hashtag" multi-select dropdown (MoMorph aKWA2klsnt). */
data class KudosHashtag(
    val id: String,
    val tag: String,
)
