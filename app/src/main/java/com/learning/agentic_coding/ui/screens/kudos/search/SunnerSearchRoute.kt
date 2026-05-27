package com.learning.agentic_coding.ui.screens.kudos.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.domain.KudosRecipient
import com.learning.agentic_coding.ui.screens.home.components.HomeTab

/**
 * Thin route adapter for the Sunner search screen. Records the picked Sunner as a recent
 * search, then forwards to [onOpenProfile] (currently a stub — no Profile screen yet).
 */
@Composable
fun SunnerSearchRoute(
    viewModel: SunnerSearchViewModel,
    onBack: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
    onOpenProfile: (KudosRecipient) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SunnerSearchScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onBack = onBack,
        onResultClick = { sunner ->
            viewModel.onResultSelected(sunner)
            onOpenProfile(sunner)
        },
        onRemoveRecent = viewModel::onRemoveRecent,
        onToggleShowAllRecents = viewModel::onToggleShowAllRecents,
        onTabClick = onTabClick,
    )
}
