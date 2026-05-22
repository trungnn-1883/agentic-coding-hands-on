package com.learning.agentic_coding.ui.screens.home

import com.learning.agentic_coding.data.awards.AwardsResult
import com.learning.agentic_coding.domain.AuthUser
import com.learning.agentic_coding.domain.Language

/**
 * Aggregate state for the Home screen — one combined snapshot per emission so the
 * UI is a pure function of state (no per-section observables in Compose).
 */
data class HomeUiState(
    val user: AuthUser? = null,
    val language: Language = Language.DEFAULT,
    val awards: AwardsResult = AwardsResult.Loading,
    val countdown: CountdownState = CountdownState.computing(),
    val isKudosAvailable: Boolean = true,
    val notificationUnread: Int = 1,
)

/**
 * Snapshot of the live event countdown — re-computed on every ticker emission.
 * [isEnded] gates the "Coming soon" copy and switches the tile block to "Event ended".
 */
data class CountdownState(
    val days: Int,
    val hours: Int,
    val minutes: Int,
    val isEnded: Boolean,
) {
    companion object {
        fun computing(): CountdownState = CountdownState(days = 0, hours = 0, minutes = 0, isEnded = false)
    }
}
