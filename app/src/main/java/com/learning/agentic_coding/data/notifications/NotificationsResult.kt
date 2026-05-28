package com.learning.agentic_coding.data.notifications

import com.learning.agentic_coding.domain.Notification

/** Snapshot of the Notifications screen payload. Mirrors KudosResult shape. */
sealed interface NotificationsResult {
    data object Loading : NotificationsResult
    data class Success(val items: List<Notification>) : NotificationsResult
    data class Error(val message: String) : NotificationsResult
}
