package com.learning.agentic_coding.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.data.awards.AwardsResult
import com.learning.agentic_coding.domain.Award
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.screens.home.components.AwardsSection
import com.learning.agentic_coding.ui.screens.home.components.HomeBottomNav
import com.learning.agentic_coding.ui.screens.home.components.HomeFab
import com.learning.agentic_coding.ui.screens.home.components.HomeHeader
import com.learning.agentic_coding.ui.screens.home.components.HomeHeroSection
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.home.components.HomeThemeDescription
import com.learning.agentic_coding.ui.screens.home.components.KudosSection

/**
 * Full Home screen — spec frame `OuH1BUTYT0` ([iOS] Home). Sections are stacked
 * vertically in a single LazyColumn so the entire surface scrolls; the FAB and
 * bottom nav float above. All click handlers are no-op by design (clarifications.md):
 * destination screens are not built yet.
 */
@Composable
fun HomeScreen(
    state: HomeUiState,
    onLanguageSelect: (Language) -> Unit,
    onRetryAwards: () -> Unit,
    onAwardClick: (Award) -> Unit = {},
    onTabClick: (HomeTab) -> Unit = {},
    onComposeKudo: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Sticky header: stays pinned while content below scrolls.
            Box(
                modifier = Modifier
                    .background(colorResource(R.color.saa_bg_dark))
                    .statusBarsPadding(),
            ) {
                HomeHeader(
                    language = state.language,
                    notificationUnread = state.notificationUnread,
                    onLanguageSelect = onLanguageSelect,
                    onSearchClick = {},
                    onNotificationsClick = {},
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    HomeHeroSection(
                        countdown = state.countdown,
                        onAboutAwardClick = {},
                        onAboutKudosClick = {},
                    )
                }
                item { HomeThemeDescription() }
                item {
                    AwardsSection(
                        state = state.awards,
                        onAwardClick = onAwardClick,
                        onRetry = onRetryAwards,
                    )
                }
                item {
                    KudosSection(
                        visible = state.isKudosAvailable,
                        onDetailsClick = {},
                    )
                }
            }
            HomeBottomNav(activeTab = HomeTab.SAA, onTabClick = onTabClick)
        }

        HomeFab(
            onPenClick = onComposeKudo,
            onKudosClick = { onTabClick(HomeTab.KUDOS) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 128.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 1500)
@Composable
private fun HomeScreenPreviewSuccess() {
    HomeScreen(
        state = HomeUiState(
            language = Language.VN,
            awards = AwardsResult.Success(
                listOf(
                    Award("1", "top-talent", "Top Talent",
                        "Giải thưởng Top Talent vinh danh những cá nhân xuất sắc trên mọi phương diện",
                        "award_top_talent", 1),
                    Award("2", "top-project", "Top Project",
                        "Giải thưởng Top Project vinh danh các tập thể dự án xuất sắc",
                        "award_top_project", 2),
                ),
            ),
            countdown = CountdownState(days = 35, hours = 12, minutes = 47, isEnded = false),
            isKudosAvailable = true,
            notificationUnread = 1,
        ),
        onLanguageSelect = {},
        onRetryAwards = {},
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 1500)
@Composable
private fun HomeScreenPreviewError() {
    HomeScreen(
        state = HomeUiState(
            language = Language.VN,
            awards = AwardsResult.Error("Network unreachable"),
            countdown = CountdownState(days = 35, hours = 12, minutes = 47, isEnded = false),
        ),
        onLanguageSelect = {},
        onRetryAwards = {},
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 1500)
@Composable
private fun HomeScreenPreviewEndedNoKudos() {
    HomeScreen(
        state = HomeUiState(
            language = Language.VN,
            awards = AwardsResult.Empty,
            countdown = CountdownState(days = 0, hours = 0, minutes = 0, isEnded = true),
            isKudosAvailable = false,
            notificationUnread = 0,
        ),
        onLanguageSelect = {},
        onRetryAwards = {},
    )
}
