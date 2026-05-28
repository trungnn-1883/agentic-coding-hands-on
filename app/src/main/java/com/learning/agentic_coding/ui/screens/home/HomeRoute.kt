package com.learning.agentic_coding.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.domain.Award
import com.learning.agentic_coding.ui.screens.home.components.HomeTab

/**
 * Thin route Composable — collects state from the [HomeViewModel] and hands it to
 * the stateless [HomeScreen]. Card-tap and bottom-nav-tab events are forwarded
 * to the host (`SaaApp`) so it can swap destinations.
 */
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onAwardClick: (Award) -> Unit,
    onTabClick: (HomeTab) -> Unit,
    onComposeKudo: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onLanguageSelect = viewModel::onLanguageSelect,
        onRetryAwards = viewModel::retryAwards,
        onAwardClick = onAwardClick,
        onTabClick = onTabClick,
        onComposeKudo = onComposeKudo,
        onNotificationsClick = onNotificationsClick,
    )
}
