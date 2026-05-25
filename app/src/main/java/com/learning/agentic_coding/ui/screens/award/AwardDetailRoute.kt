package com.learning.agentic_coding.ui.screens.award

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.ui.screens.home.components.HomeTab

/**
 * Thin route Composable for Award Detail. Mirrors [com.learning.agentic_coding.ui.screens.home.HomeRoute]:
 * collects VM state and forwards callbacks. Bottom-nav tab clicks are bubbled up to the host
 * (`SaaApp`) so it can swap destinations (SAA tab → Home, etc.).
 */
@Composable
fun AwardDetailRoute(
    viewModel: AwardDetailViewModel,
    onTabClick: (HomeTab) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AwardDetailScreen(
        state = state,
        onLanguageSelect = viewModel::onLanguageSelect,
        onDropdownOpenChange = viewModel::setDropdownOpen,
        onAwardSelect = { viewModel.selectAward(it.slug) },
        onRetry = viewModel::retry,
        onTabClick = onTabClick,
    )
}
