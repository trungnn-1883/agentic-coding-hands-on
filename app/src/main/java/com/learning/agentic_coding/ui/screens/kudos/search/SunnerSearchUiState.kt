package com.learning.agentic_coding.ui.screens.kudos.search

import com.learning.agentic_coding.domain.KudosRecipient

/**
 * UI state for the Sunner search screen (MoMorph 3jgwke3E8O default + hldqjHoSRH active).
 *
 * - Blank [query] → show [recents] under the "Recent" header.
 * - Non-blank [query] → show [results]; when [searching] is false and results are empty,
 *   show the empty-state message.
 */
data class SunnerSearchUiState(
    val query: String = "",
    val results: List<KudosRecipient> = emptyList(),
    val recents: List<KudosRecipient> = emptyList(),
    val searching: Boolean = false,
    val showAllRecents: Boolean = false,
) {
    val isQueryActive: Boolean get() = query.isNotBlank()
    val showEmptyState: Boolean get() = isQueryActive && !searching && results.isEmpty()
    val visibleRecents: List<KudosRecipient>
        get() = if (showAllRecents) recents else recents.take(DEFAULT_RECENT_VISIBLE)

    companion object {
        const val DEFAULT_RECENT_VISIBLE = 2
    }
}
