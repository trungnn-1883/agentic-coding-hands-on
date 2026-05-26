package com.learning.agentic_coding.ui.screens.kudos.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.ui.screens.home.components.HomeTab

/** Thin route adapter for the Kudo detail screen. */
@Composable
fun KudoDetailRoute(
    viewModel: KudoDetailViewModel,
    onBack: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    KudoDetailScreen(
        state = state,
        onBack = onBack,
        onRetry = viewModel::retry,
        onHeartToggle = viewModel::toggleHeart,
        onTabClick = onTabClick,
    )
}
