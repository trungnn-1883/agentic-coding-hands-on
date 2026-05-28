package com.learning.agentic_coding.ui.screens.kudos.secretbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** Route adapter: collects [OpenBoxViewModel] state and forwards intents to the screen. */
@Composable
fun OpenBoxRoute(
    viewModel: OpenBoxViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    OpenBoxScreen(
        state = state,
        onBack = onBack,
        onBoxTap = viewModel::onBoxTap,
        onDismissReveal = viewModel::onDismissReveal,
        onRetry = viewModel::retry,
    )
}
