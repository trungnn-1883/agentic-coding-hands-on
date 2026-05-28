package com.learning.agentic_coding.data.notifications

import com.learning.agentic_coding.data.kudos.parsePostgrestTimestamp
import com.learning.agentic_coding.domain.Notification
import com.learning.agentic_coding.domain.NotificationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Wire shape for `notifications` rows. */
@Serializable
internal data class NotificationRow(
    val id: String,
    val type: String,
    val content: String,
    @SerialName("target_id") val targetId: String? = null,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("display_order") val displayOrder: Int = 0,
    @SerialName("created_at") val createdAt: String,
) {
    fun toDomain(): Notification = Notification(
        id = id,
        type = NotificationType.fromWire(type),
        content = content,
        targetId = targetId,
        isRead = isRead,
        createdAt = parsePostgrestTimestamp(createdAt),
        displayOrder = displayOrder,
    )
}

/** Update payload for toggling read state. */
@Serializable
internal data class NotificationReadPatch(
    @SerialName("is_read") val isRead: Boolean,
)
