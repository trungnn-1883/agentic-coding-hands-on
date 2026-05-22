package com.learning.agentic_coding.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Thin route Composable — collects state from the [HomeViewModel] and hands it to
 * the stateless [HomeScreen]. Keeps preview/testing of [HomeScreen] possible
 * without the ViewModel.
 */
@Composable
fun HomeRoute(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onLanguageSelect = viewModel::onLanguageSelect,
        onRetryAwards = viewModel::retryAwards,
    )
}
