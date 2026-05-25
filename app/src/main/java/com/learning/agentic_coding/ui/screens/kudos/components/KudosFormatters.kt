package com.learning.agentic_coding.ui.screens.kudos.components

import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosBadge
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** Stable presentation helpers shared by KudosHome and AllKudos screens. */
internal object KudosFormatters {

    private val cardTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("HH:mm - MM/dd/yyyy")

    private val displayZone: ZoneId = ZoneId.of("Asia/Saigon")

    fun cardTimestamp(postedAt: OffsetDateTime): String =
        postedAt.atZoneSameInstant(displayZone).format(cardTimeFormatter)

    fun badgeLabelRes(badge: KudosBadge): Int = when (badge) {
        KudosBadge.RisingHero -> R.string.kudos_badge_rising_hero
        KudosBadge.LegendHero -> R.string.kudos_badge_legend_hero
    }

    /** Stable accent color per name — used to tint avatar placeholders consistently. */
    fun avatarColorIndex(name: String): Int {
        val sum = name.fold(0) { acc, ch -> acc + ch.code }
        return sum.mod(AVATAR_COUNT)
    }

    const val AVATAR_COUNT: Int = 6
}
