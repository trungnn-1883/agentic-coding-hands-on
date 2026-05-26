package com.learning.agentic_coding.ui.screens.kudos.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.data.kudos.KudosCatalog
import com.learning.agentic_coding.data.kudos.KudosResult
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.screens.home.components.HomeBottomNav
import com.learning.agentic_coding.ui.screens.home.components.HomeHeader
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.kudos.components.KudosCard
import com.learning.agentic_coding.ui.screens.kudos.main.components.KudosGiftPanel
import com.learning.agentic_coding.ui.screens.kudos.main.components.KudosHero
import com.learning.agentic_coding.ui.screens.kudos.main.components.KudosHighlightCarousel
import com.learning.agentic_coding.ui.screens.kudos.components.KudosKvBackground
import com.learning.agentic_coding.ui.screens.kudos.components.KudosSectionHeading
import com.learning.agentic_coding.ui.screens.kudos.main.components.KudosSpotlightBoard
import com.learning.agentic_coding.ui.screens.kudos.main.components.KudosStatsCard

/**
 * Kudos home — composed of: header, KUDOS hero, HIGHLIGHT KUDOS (filters + carousel),
 * SPOTLIGHT BOARD, ALL KUDOS stats + Secret Box, gift recipients panel, recent posts,
 * "View all Kudos" link, bottom nav. Maps to MoMorph frames 76k69LQPfj / V5GRjAdJyb.
 */
@Composable
fun KudosHomeScreen(
    state: KudosHomeUiState,
    onLanguageSelect: (Language) -> Unit,
    onDeptSelect: (String?) -> Unit,
    onHashtagSelect: (String?) -> Unit,
    onViewAllKudos: () -> Unit,
    onRetry: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(colorResource(R.color.saa_bg_dark))
                    .statusBarsPadding(),
            ) {
                HomeHeader(
                    language = state.language,
                    notificationUnread = 1,
                    onLanguageSelect = onLanguageSelect,
                    onSearchClick = {},
                    onNotificationsClick = {},
                )
            }
            KudosHomeBody(
                state = state,
                onDeptSelect = onDeptSelect,
                onHashtagSelect = onHashtagSelect,
                onViewAllKudos = onViewAllKudos,
                onRetry = onRetry,
                onDetailClick = onDetailClick,
                modifier = Modifier.weight(1f),
            )
            HomeBottomNav(activeTab = HomeTab.KUDOS, onTabClick = onTabClick)
        }
    }
}

@Composable
private fun KudosHomeBody(
    state: KudosHomeUiState,
    onDeptSelect: (String?) -> Unit,
    onHashtagSelect: (String?) -> Unit,
    onViewAllKudos: () -> Unit,
    onRetry: () -> Unit,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (val data = state.data) {
        is KudosResult.Loading -> LoadingPlaceholder(modifier = modifier)
        is KudosResult.Error -> ErrorPlaceholder(message = data.message, onRetry = onRetry, modifier = modifier)
        is KudosResult.Success -> KudosHomeContent(
            state = state,
            data = data,
            onDeptSelect = onDeptSelect,
            onHashtagSelect = onHashtagSelect,
            onViewAllKudos = onViewAllKudos,
            onDetailClick = onDetailClick,
            modifier = modifier,
        )
    }
}


@Composable
private fun KudosHomeContent(
    state: KudosHomeUiState,
    data: KudosResult.Success,
    onDeptSelect: (String?) -> Unit,
    onHashtagSelect: (String?) -> Unit,
    onViewAllKudos: () -> Unit,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val highlightPosts = data.posts.filter { it.isHighlight }
    // Recent = non-highlight posts to avoid duplicating the cards already shown in the carousel.
    val recentPosts = data.posts.filterNot { it.isHighlight }.take(3)
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            // KV image background bleeds behind the hero and section heading. Box ensures it sits underneath.
            Box {
                KudosKvBackground()
                Column {
                    KudosHero(modifier = Modifier.padding(top = 12.dp))
                    Box(modifier = Modifier.height(40.dp))
                    KudosSectionHeading(title = stringResource(R.string.kudos_highlight_title))
                    Box(modifier = Modifier.height(24.dp))
                    KudosHighlightCarousel(
                        posts = highlightPosts,
                        selectedDept = state.departmentFilter,
                        selectedHashtag = state.hashtagFilter,
                        departmentOptions = KudosCatalog.departments,
                        hashtagOptions = KudosCatalog.hashtags,
                        onDeptSelect = onDeptSelect,
                        onHashtagSelect = onHashtagSelect,
                        onDetailClick = onDetailClick,
                    )
                }
            }
        }

        item { KudosSectionHeading(title = stringResource(R.string.kudos_spotlight_title)) }
        item { KudosSpotlightBoard() }
        item { KudosSectionHeading(title = stringResource(R.string.kudos_all_title)) }
        item { KudosStatsCard(stats = data.stats, onOpenSecretBox = {}) }
        item { KudosGiftPanel(recipients = data.giftRecipients) }
        items(recentPosts, key = { it.id }) { post ->
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                KudosCard(post = post, onDetailClick = onDetailClick)
            }
        }
        item { ViewAllKudosLink(onClick = onViewAllKudos) }
    }
}

@Composable
private fun ViewAllKudosLink(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.kudos_view_all),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_external),
            contentDescription = null,
            tint = colorResource(R.color.saa_text_primary),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(16.dp),
        )
    }
}

@Composable
private fun LoadingPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "...",
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun ErrorPlaceholder(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = colorResource(R.color.saa_error),
            fontSize = 14.sp,
        )
        Text(
            text = stringResource(R.string.home_awards_retry),
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable(onClick = onRetry),
        )
    }
}
