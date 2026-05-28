package com.learning.agentic_coding.ui.screens.kudos.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.ui.screens.home.components.HomeTab

/** Thin route adapter — collects KudosHomeViewModel state and hands events to host. */
@Composable
fun KudosHomeRoute(
    viewModel: KudosHomeViewModel,
    onLanguageSelect: (com.learning.agentic_coding.domain.Language) -> Unit,
    onViewAllKudos: () -> Unit,
    onDetailClick: (String) -> Unit,
    onTabClick: (HomeTab) -> Unit,
    onComposeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onRulesClick: () -> Unit = {},
    onOpenSecretBox: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    KudosHomeScreen(
        state = state,
        onLanguageSelect = onLanguageSelect,
        onDeptSelect = viewModel::selectDepartment,
        onHashtagSelect = viewModel::selectHashtag,
        onViewAllKudos = onViewAllKudos,
        onRetry = viewModel::retry,
        onTabClick = onTabClick,
        onDetailClick = onDetailClick,
        onComposeClick = onComposeClick,
        onSearchClick = onSearchClick,
        onRulesClick = onRulesClick,
        onOpenSecretBox = onOpenSecretBox,
        onNotificationsClick = onNotificationsClick,
    )
}
