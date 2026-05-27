package com.learning.agentic_coding.ui.screens.kudos.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.ui.screens.home.components.HomeTab

/**
 * Wraps the Send-Kudos screen. After a successful insert, fires [onSent] so the parent
 * (SaaApp) can route the user back to the Kudos home feed.
 */
@Composable
fun KudoComposeRoute(
    viewModel: KudoComposeViewModel,
    onBack: () -> Unit,
    onSent: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.submitState) {
        if (state.submitState is SubmitState.Success) {
            onSent()
            viewModel.reset()
        }
    }

    KudoComposeScreen(
        state = state,
        onBack = onBack,
        onRecipientQuery = viewModel::onRecipientQuery,
        onRecipientSelect = viewModel::onRecipientSelect,
        onTitleChange = viewModel::onTitleChange,
        onBodyChange = viewModel::onBodyChange,
        onBodySpansChange = viewModel::onBodySpansChange,
        onPendingFormatsChange = viewModel::onPendingFormatsChange,
        onHashtagToggle = viewModel::onHashtagToggle,
        onHashtagRemove = viewModel::onHashtagRemove,
        onImagesPicked = { uris -> viewModel.onImagesPicked(context.contentResolver, uris) },
        onImageRemove = viewModel::onImageRemove,
        onAnonymousToggle = viewModel::onAnonymousToggle,
        onAnonNicknameChange = viewModel::onAnonNicknameChange,
        onSubmit = viewModel::onSubmit,
        onTabClick = onTabClick,
    )
}
