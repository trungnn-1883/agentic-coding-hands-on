package com.learning.agentic_coding.ui.screens.notifications.components

import android.content.res.Resources
import com.learning.agentic_coding.R
import java.time.Duration
import java.time.OffsetDateTime

/**
 * Formats a notification's `createdAt` as a coarse relative time label
 * ("15 phút trước", "1 giờ trước", "1 ngày trước", "1 tháng trước").
 * Mirrors the granularity shown in MoMorph [iOS] Notifications.
 */
fun formatRelativeTime(
    resources: Resources,
    createdAt: OffsetDateTime,
    now: OffsetDateTime = OffsetDateTime.now(),
): String {
    val seconds = Duration.between(createdAt, now).seconds.coerceAtLeast(0)
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30
    return when {
        months >= 1 -> resources.getString(R.string.notifications_time_months_ago, months.toInt())
        days >= 1 -> resources.getString(R.string.notifications_time_days_ago, days.toInt())
        hours >= 1 -> resources.getString(R.string.notifications_time_hours_ago, hours.toInt())
        minutes >= 1 -> resources.getString(R.string.notifications_time_minutes_ago, minutes.toInt())
        else -> resources.getString(R.string.notifications_time_just_now)
    }
}
