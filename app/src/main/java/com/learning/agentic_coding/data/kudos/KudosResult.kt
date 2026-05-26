package com.learning.agentic_coding.data.kudos

import com.learning.agentic_coding.domain.KudosGiftRecipient
import com.learning.agentic_coding.domain.KudosPost
import com.learning.agentic_coding.domain.KudosUserStats

/** Snapshot of the Kudos home payload. Loading/Error states keep parity with [AwardsResult]. */
sealed interface KudosResult {
    data object Loading : KudosResult
    data class Success(
        val posts: List<KudosPost>,
        val stats: KudosUserStats,
        val giftRecipients: List<KudosGiftRecipient>,
    ) : KudosResult
    data class Error(val message: String) : KudosResult
}

/** Single-kudo payload for the detail screen. */
sealed interface KudoDetailResult {
    data object Loading : KudoDetailResult
    data class Success(val post: KudosPost) : KudoDetailResult
    data class Error(val message: String) : KudoDetailResult
}

/** Static catalog values shown in the dropdown menus (see clarifications.md). */
object KudosCatalog {
    val departments: List<String> = listOf("CECV1", "CECV2", "CECV3", "CECV4", "OPD", "Infra")
    val hashtags: List<String> = listOf("#Dedicated", "#Inspring")
}
