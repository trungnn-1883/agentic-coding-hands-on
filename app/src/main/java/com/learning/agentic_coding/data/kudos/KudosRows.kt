package com.learning.agentic_coding.data.kudos

import com.learning.agentic_coding.domain.KudosBadge
import com.learning.agentic_coding.domain.KudosGiftRecipient
import com.learning.agentic_coding.domain.KudosParty
import com.learning.agentic_coding.domain.KudosPost
import com.learning.agentic_coding.domain.KudosUserStats
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Wire shape for `kudos_posts` rows. */
@Serializable
internal data class KudosPostRow(
    val id: String,
    @SerialName("sender_name") val senderName: String,
    @SerialName("sender_dept") val senderDept: String,
    @SerialName("sender_badge") val senderBadge: String,
    @SerialName("receiver_name") val receiverName: String,
    @SerialName("receiver_dept") val receiverDept: String,
    @SerialName("receiver_badge") val receiverBadge: String,
    val title: String,
    val content: String,
    val hashtags: List<String> = emptyList(),
    val hearts: Int = 0,
    @SerialName("posted_at") val postedAt: String,
    @SerialName("is_highlight") val isHighlight: Boolean = false,
    @SerialName("display_order") val displayOrder: Int = 0,
) {
    fun toDomain(): KudosPost = KudosPost(
        id = id,
        sender = KudosParty(senderName, senderDept, KudosBadge.fromWire(senderBadge)),
        receiver = KudosParty(receiverName, receiverDept, KudosBadge.fromWire(receiverBadge)),
        title = title,
        content = content,
        hashtags = hashtags,
        hearts = hearts,
        postedAt = parsePostgrestTimestamp(postedAt),
        isHighlight = isHighlight,
    )
}

@Serializable
internal data class KudosUserStatsRow(
    @SerialName("kudos_received") val kudosReceived: Int = 0,
    @SerialName("kudos_sent") val kudosSent: Int = 0,
    @SerialName("hearts_received") val heartsReceived: Int = 0,
    @SerialName("hearts_multiplier") val heartsMultiplier: Int = 1,
    @SerialName("secret_box_opened") val secretBoxOpened: Int = 0,
    @SerialName("secret_box_unopened") val secretBoxUnopened: Int = 0,
) {
    fun toDomain(): KudosUserStats = KudosUserStats(
        kudosReceived, kudosSent, heartsReceived, heartsMultiplier,
        secretBoxOpened, secretBoxUnopened,
    )
}

/**
 * PostgREST normally emits `2026-05-25T10:00:00+00:00`, but older versions emit `+0000`
 * (no colon). Try the standard ISO parser first, fall back to a custom formatter, and as
 * a last resort default to "now" so a wire glitch doesn't blank the whole screen.
 */
private val POSTGREST_FALLBACK_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSS]Z")

internal fun parsePostgrestTimestamp(raw: String): OffsetDateTime =
    runCatching { OffsetDateTime.parse(raw) }
        .recoverCatching { OffsetDateTime.parse(raw, POSTGREST_FALLBACK_FORMATTER) }
        .getOrElse { OffsetDateTime.now(ZoneOffset.UTC) }

@Serializable
internal data class KudosGiftRecipientRow(
    @SerialName("recipient_name") val recipientName: String,
    @SerialName("gift_text") val giftText: String,
    @SerialName("display_order") val displayOrder: Int = 0,
) {
    fun toDomain(): KudosGiftRecipient = KudosGiftRecipient(recipientName, giftText)
}
