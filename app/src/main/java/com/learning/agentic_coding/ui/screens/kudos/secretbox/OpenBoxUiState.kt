package com.learning.agentic_coding.ui.screens.kudos.secretbox

import com.learning.agentic_coding.domain.SecretBoxReward

/**
 * State machine for the Open Secret Box screen.
 *
 * - [Loading]   initial stats fetch in flight
 * - [Idle]      box visible; tap to open (no-op when unopenedCount == 0)
 * - [Opening]   waiting on backend after tap (brief spinner)
 * - [Revealed]  reward shown; tap anywhere returns to [Idle]
 * - [Failure]   load/open error
 */
sealed interface OpenBoxUiState {
    data object Loading : OpenBoxUiState
    data class Idle(val unopenedCount: Int) : OpenBoxUiState
    data class Opening(val unopenedCount: Int) : OpenBoxUiState
    data class Revealed(val reward: SecretBoxReward, val unopenedCount: Int) : OpenBoxUiState
    data class Failure(val message: String) : OpenBoxUiState
}
