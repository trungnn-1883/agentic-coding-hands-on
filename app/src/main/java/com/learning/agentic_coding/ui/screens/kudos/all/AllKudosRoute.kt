package com.learning.agentic_coding.ui.screens.kudos.all

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.ui.screens.home.components.HomeTab

/** Thin route adapter for the All Kudos detail screen. */
@Composable
fun AllKudosRoute(
    viewModel: AllKudosViewModel,
    onBack: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AllKudosScreen(
        state = state,
        onBack = onBack,
        onRetry = viewModel::retry,
        onTabClick = onTabClick,
    )
}
