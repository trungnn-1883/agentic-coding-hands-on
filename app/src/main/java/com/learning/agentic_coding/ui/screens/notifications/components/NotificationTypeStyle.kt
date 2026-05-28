package com.learning.agentic_coding.ui.screens.notifications.components

import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.NotificationType

/**
 * Resolves icon drawable + tint color resource per [NotificationType] — extracted from
 * MoMorph [iOS] Notifications design item B.1.1.
 */
data class NotificationTypeStyle(
    val iconRes: Int,
    val tintColorRes: Int,
)

fun NotificationType.style(): NotificationTypeStyle = when (this) {
    NotificationType.KUDOS_RECEIVED -> NotificationTypeStyle(R.drawable.ic_notif_envelope_plus, R.color.saa_notif_envelope)
    NotificationType.HEART_RECEIVED -> NotificationTypeStyle(R.drawable.ic_notif_heart_plus, R.color.saa_notif_heart)
    NotificationType.SECRET_BOX -> NotificationTypeStyle(R.drawable.ic_notif_gift_box, R.color.saa_notif_gift)
    NotificationType.LEVEL_UP -> NotificationTypeStyle(R.drawable.ic_notif_star_outline, R.color.saa_notif_star)
    NotificationType.CONTENT_HIDDEN -> NotificationTypeStyle(R.drawable.ic_notif_warning, R.color.saa_notif_warning)
    NotificationType.BADGE_COLLECTED -> NotificationTypeStyle(R.drawable.ic_notif_badge_check, R.color.saa_notif_note)
    NotificationType.REVIEW_REQUEST -> NotificationTypeStyle(R.drawable.ic_notif_pen, R.color.saa_notif_flag)
}
