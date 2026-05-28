package com.learning.agentic_coding.domain

import java.time.OffsetDateTime

/**
 * One row on the [iOS] Notifications screen (_b68CBWKl5). [type] drives icon, tint, and
 * navigation; [targetId] points to the entity to open on tap (e.g., kudo id), null when no
 * destination is built yet.
 */
data class Notification(
    val id: String,
    val type: NotificationType,
    val content: String,
    val targetId: String?,
    val isRead: Boolean,
    val createdAt: OffsetDateTime,
    val displayOrder: Int,
)

enum class NotificationType(val wire: String) {
    KUDOS_RECEIVED("kudos_received"),
    HEART_RECEIVED("heart_received"),
    SECRET_BOX("secret_box"),
    LEVEL_UP("level_up"),
    CONTENT_HIDDEN("content_hidden"),
    BADGE_COLLECTED("badge_collected"),
    REVIEW_REQUEST("review_request");

    companion object {
        fun fromWire(raw: String): NotificationType =
            entries.firstOrNull { it.wire == raw } ?: KUDOS_RECEIVED
    }
}
