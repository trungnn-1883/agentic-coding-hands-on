package com.learning.agentic_coding.ui.screens.award

import com.learning.agentic_coding.data.awards.AwardsResult
import com.learning.agentic_coding.domain.Award
import com.learning.agentic_coding.domain.Language

/**
 * Combined state for the Award Detail screen. [selectedAward] is derived from
 * [awards] + [selectedSlug] so consumers don't have to compute it themselves.
 */
data class AwardDetailUiState(
    val awards: AwardsResult = AwardsResult.Loading,
    val selectedSlug: String = "",
    val language: Language = Language.DEFAULT,
    val notificationUnread: Int = 1,
    val isDropdownOpen: Boolean = false,
) {
    val allAwards: List<Award>
        get() = (awards as? AwardsResult.Success)?.awards.orEmpty()

    /** Falls back to the first award if the requested slug isn't present yet. */
    val selectedAward: Award?
        get() {
            val list = allAwards
            return list.firstOrNull { it.slug == selectedSlug } ?: list.firstOrNull()
        }
}
