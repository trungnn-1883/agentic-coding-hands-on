package com.learning.agentic_coding.domain

import java.time.OffsetDateTime

/**
 * One published Kudos card surfaced on the Kudos home page and All-Kudos detail screen
 * (MoMorph frames j_a2GQWKDJ / 76k69LQPfj / V5GRjAdJyb).
 */
data class KudosPost(
    val id: String,
    val sender: KudosParty,
    val receiver: KudosParty,
    val title: String,
    val content: String,
    val hashtags: List<String>,
    val hearts: Int,
    val postedAt: OffsetDateTime,
    val isHighlight: Boolean,
    val attachedImages: List<String> = emptyList(),
    val isAnonymous: Boolean = false,
    val anonNickname: String? = null,
)

/** Sender or receiver side of a Kudos card (name + dept code + badge). */
data class KudosParty(
    val name: String,
    val dept: String,
    val badge: KudosBadge,
    val avatarUrl: String? = null,
)

enum class KudosBadge(val wire: String) {
    RisingHero("rising_hero"),
    LegendHero("legend_hero");

    companion object {
        fun fromWire(value: String): KudosBadge =
            entries.firstOrNull { it.wire == value } ?: RisingHero
    }
}

/** Per-user aggregate displayed in the ALL KUDOS stats card. */
data class KudosUserStats(
    val kudosReceived: Int,
    val kudosSent: Int,
    val heartsReceived: Int,
    val heartsMultiplier: Int,
    val secretBoxOpened: Int,
    val secretBoxUnopened: Int,
)

/** Recent gift-recipient row shown in the "10 SUNNER NHẬN QUÀ MỚI NHẤT" panel. */
data class KudosGiftRecipient(
    val name: String,
    val giftText: String,
)
