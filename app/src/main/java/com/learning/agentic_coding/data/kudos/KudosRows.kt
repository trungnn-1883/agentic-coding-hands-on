package com.learning.agentic_coding.data.kudos

import com.learning.agentic_coding.domain.KudosBadge
import com.learning.agentic_coding.domain.KudosGiftRecipient
import com.learning.agentic_coding.domain.KudosHashtag
import com.learning.agentic_coding.domain.KudosParty
import com.learning.agentic_coding.domain.KudosPost
import com.learning.agentic_coding.domain.KudosRecipient
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
    @SerialName("sender_avatar_url") val senderAvatarUrl: String? = null,
    @SerialName("receiver_name") val receiverName: String,
    @SerialName("receiver_dept") val receiverDept: String,
    @SerialName("receiver_badge") val receiverBadge: String,
    @SerialName("receiver_avatar_url") val receiverAvatarUrl: String? = null,
    val title: String,
    val content: String,
    val hashtags: List<String> = emptyList(),
    val hearts: Int = 0,
    @SerialName("posted_at") val postedAt: String,
    @SerialName("is_highlight") val isHighlight: Boolean = false,
    @SerialName("display_order") val displayOrder: Int = 0,
    @SerialName("is_anonymous") val isAnonymous: Boolean = false,
    @SerialName("anon_nickname") val anonNickname: String? = null,
) {
    fun toDomain(attachedImages: List<String> = emptyList()): KudosPost = KudosPost(
        id = id,
        sender = KudosParty(senderName, senderDept, KudosBadge.fromWire(senderBadge), senderAvatarUrl),
        receiver = KudosParty(receiverName, receiverDept, KudosBadge.fromWire(receiverBadge), receiverAvatarUrl),
        title = title,
        content = content,
        hashtags = hashtags,
        hearts = hearts,
        postedAt = parsePostgrestTimestamp(postedAt),
        isHighlight = isHighlight,
        attachedImages = attachedImages,
        isAnonymous = isAnonymous,
        anonNickname = anonNickname,
    )
}

/** Wire shape for `kudos_recipients` rows. */
@Serializable
internal data class KudosRecipientRow(
    val id: String,
    val email: String? = null,
    val name: String,
    val dept: String,
    val badge: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("display_order") val displayOrder: Int = 0,
) {
    fun toDomain(): KudosRecipient = KudosRecipient(
        id = id,
        email = email,
        name = name,
        dept = dept,
        badge = KudosBadge.fromWire(badge),
        avatarUrl = avatarUrl,
    )
}

/** Wire shape for `kudos_hashtags` rows. */
@Serializable
internal data class KudosHashtagRow(
    val id: String,
    val tag: String,
    @SerialName("display_order") val displayOrder: Int = 0,
) {
    fun toDomain(): KudosHashtag = KudosHashtag(id = id, tag = tag)
}

/** Insert payload for `kudos_posts`. id/posted_at default-generated server-side. */
@Serializable
data class KudosPostInsert(
    @SerialName("sender_name") val senderName: String,
    @SerialName("sender_dept") val senderDept: String,
    @SerialName("sender_badge") val senderBadge: String,
    @SerialName("sender_avatar_url") val senderAvatarUrl: String? = null,
    @SerialName("receiver_name") val receiverName: String,
    @SerialName("receiver_dept") val receiverDept: String,
    @SerialName("receiver_badge") val receiverBadge: String,
    @SerialName("receiver_avatar_url") val receiverAvatarUrl: String? = null,
    val title: String,
    val content: String,
    val hashtags: List<String>,
    @SerialName("is_anonymous") val isAnonymous: Boolean = false,
    @SerialName("anon_nickname") val anonNickname: String? = null,
)

@Serializable
internal data class KudoImageInsert(
    @SerialName("kudo_id") val kudoId: String,
    @SerialName("image_url") val imageUrl: String,
    // No default: kotlinx omits default-valued props, which would drop sort_order for the
    // idx=0 row and trip the NOT-NULL constraint in a multi-row insert. Always serialize it.
    @SerialName("sort_order") val sortOrder: Int,
)

@Serializable
internal data class KudosInsertedRow(val id: String)

@Serializable
internal data class KudoImageRow(
    @SerialName("kudo_id") val kudoId: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("sort_order") val sortOrder: Int = 0,
)

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
