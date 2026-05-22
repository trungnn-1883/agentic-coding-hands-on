package com.learning.agentic_coding.data.kudos

/**
 * Single source of truth for the Kudos feature flag (spec TC_GUI_005 / FUN_009).
 * Hardcoded `true` per clarifications.md — the section always renders.
 * Flip this constant to verify the hidden state.
 */
class KudosRepository {
    val isKudosAvailable: Boolean = true
}
