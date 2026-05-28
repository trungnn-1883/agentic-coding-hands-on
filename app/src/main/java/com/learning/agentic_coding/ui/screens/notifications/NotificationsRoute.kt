package com.learning.agentic_coding.ui.screens.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.domain.Notification

/**
 * VM-bound entry point for [NotificationsScreen]. Resolves per-type navigation and forwards
 * the mark-as-read intent before deferring to the parent for actual destination switching.
 */
@Composable
fun NotificationsRoute(
    viewModel: NotificationsViewModel,
    onBack: () -> Unit,
    onOpenKudoDetail: (String) -> Unit,
    onOpenSecretBox: () -> Unit,
    onOpenCommunityStandards: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    NotificationsScreen(
        state = state,
        onBack = onBack,
        onItemClick = { item ->
            viewModel.markAsRead(item.id)
            navigateForType(item, onOpenKudoDetail, onOpenSecretBox)
        },
        onCommunityStandardsClick = onOpenCommunityStandards,
        onMarkAllRead = viewModel::markAllRead,
        onRetry = viewModel::retry,
    )
}

private fun navigateForType(
    item: Notification,
    onOpenKudoDetail: (String) -> Unit,
    onOpenSecretBox: () -> Unit,
) {
    val targetId = item.targetId
    when (item.type) {
        com.learning.agentic_coding.domain.NotificationType.KUDOS_RECEIVED,
        com.learning.agentic_coding.domain.NotificationType.HEART_RECEIVED,
        com.learning.agentic_coding.domain.NotificationType.CONTENT_HIDDEN -> {
            if (!targetId.isNullOrBlank()) onOpenKudoDetail(targetId)
        }
        com.learning.agentic_coding.domain.NotificationType.SECRET_BOX -> onOpenSecretBox()
        // LEVEL_UP / BADGE_COLLECTED / REVIEW_REQUEST — destinations not built; mark-read only.
        else -> Unit
    }
}
