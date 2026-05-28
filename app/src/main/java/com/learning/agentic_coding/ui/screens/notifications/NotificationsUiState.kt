package com.learning.agentic_coding.ui.screens.notifications

import com.learning.agentic_coding.data.notifications.NotificationsResult
import com.learning.agentic_coding.domain.Language

/** UI state for [NotificationsScreen]. `language` drives relative-time labels. */
data class NotificationsUiState(
    val language: Language = Language.DEFAULT,
    val data: NotificationsResult = NotificationsResult.Loading,
)
