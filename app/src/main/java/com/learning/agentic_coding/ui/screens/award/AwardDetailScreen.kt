package com.learning.agentic_coding.ui.screens.award

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.data.awards.AwardsResult
import com.learning.agentic_coding.domain.Award
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.screens.award.components.AwardHighlightBlock
import com.learning.agentic_coding.ui.screens.award.components.AwardInfoBlock
import com.learning.agentic_coding.ui.screens.award.components.AwardKvBanner
import com.learning.agentic_coding.ui.screens.home.components.HomeBottomNav
import com.learning.agentic_coding.ui.screens.home.components.HomeHeader
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.home.components.KudosSection

/**
 * Award detail screen — shared by all 6 MoMorph award frames (Top Talent / MVP /
 * Top Project / Top Project Leader / Best Manager / Signature 2025 - Creator).
 * Layout: keyvisual artwork behind header + KV banner + Highlight block, then
 * Award info block + Kudos section on the dark background, with sticky bottom nav.
 */
@Composable
fun AwardDetailScreen(
    state: AwardDetailUiState,
    onLanguageSelect: (Language) -> Unit,
    onDropdownOpenChange: (Boolean) -> Unit,
    onAwardSelect: (Award) -> Unit,
    onRetry: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
    onNotificationsClick: () -> Unit = {},
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
                    .fillMaxWidth()
                    .background(colorResource(R.color.saa_bg_dark))
                    .statusBarsPadding(),
            ) {
                HomeHeader(
                    language = state.language,
                    notificationUnread = state.notificationUnread,
                    onLanguageSelect = onLanguageSelect,
                    onSearchClick = {},
                    onNotificationsClick = onNotificationsClick,
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                item { ScrollingHero(state = state, onDropdownOpenChange = onDropdownOpenChange, onAwardSelect = onAwardSelect) }
                item { Box(modifier = Modifier.height(40.dp)) }
                item { BodyContent(state = state, onRetry = onRetry) }
                item { Box(modifier = Modifier.height(16.dp)) }
                item {
                    KudosSection(visible = true, onDetailsClick = {})
                }
                item { Box(modifier = Modifier.height(24.dp)) }
            }
            HomeBottomNav(activeTab = HomeTab.AWARDS, onTabClick = onTabClick)
        }
    }
}

/** Scrollable hero — keyvisual background + KV banner + highlight block. Header is hoisted above. */
@Composable
private fun ScrollingHero(
    state: AwardDetailUiState,
    onDropdownOpenChange: (Boolean) -> Unit,
    onAwardSelect: (Award) -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.bg_keyvisual),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopEnd,
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(heroDimGradient()),
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            AwardKvBanner()
            AwardHighlightBlock(
                selectedSlug = state.selectedAward?.slug.orEmpty(),
                language = state.language,
                awards = state.allAwards,
                dropdownOpen = state.isDropdownOpen,
                onDropdownOpenChange = onDropdownOpenChange,
                onAwardSelect = onAwardSelect,
            )
        }
    }
}

@Composable
private fun BodyContent(state: AwardDetailUiState, onRetry: () -> Unit) {
    when (state.awards) {
        is AwardsResult.Loading -> CenteredBox {
            CircularProgressIndicator(color = colorResource(R.color.saa_button_yellow))
        }
        is AwardsResult.Empty -> CenteredBox {
            Text(
                text = stringResource(R.string.home_awards_empty),
                color = colorResource(R.color.saa_text_secondary),
                fontSize = 14.sp,
            )
        }
        is AwardsResult.Error -> CenteredBox {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.home_awards_error),
                    color = colorResource(R.color.saa_text_secondary),
                    fontSize = 14.sp,
                )
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.saa_button_yellow),
                        contentColor = colorResource(R.color.saa_text_on_button),
                    ),
                    modifier = Modifier.padding(top = 12.dp),
                ) {
                    Text(stringResource(R.string.home_awards_retry), fontSize = 13.sp)
                }
            }
        }
        is AwardsResult.Success -> state.selectedAward?.let { award ->
            AwardInfoBlock(award = award, language = state.language)
        }
    }
}

@Composable
private fun CenteredBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.TopCenter,
    ) { content() }
}

private fun heroDimGradient(): Brush = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0x9900101A),
        0.7f to Color(0xCC00101A),
        1.0f to Color(0xFF00101A),
    ),
)

@Preview(showBackground = true, widthDp = 375, heightDp = 1500)
@Composable
private fun AwardDetailPreviewTopTalent() {
    val award = Award(
        id = "1",
        slug = "top-talent",
        name = "Top Talent",
        shortDescription = "Short",
        imageDrawable = "award_top_talent",
        displayOrder = 1,
        longDescription = "Giải thưởng Top Talent vinh danh những cá nhân xuất sắc toàn diện – luôn khẳng định chuyên môn vững vàng, hiệu suất vượt trội và tinh thần học hỏi không ngừng.",
        quantity = 10,
        quantityUnit = "Cá nhân",
        prizeValue = "7.000.000 VNĐ",
        prizeNote = "cho mỗi giải thưởng",
    )
    AwardDetailScreen(
        state = AwardDetailUiState(
            awards = AwardsResult.Success(listOf(award)),
            selectedSlug = "top-talent",
        ),
        onLanguageSelect = {},
        onDropdownOpenChange = {},
        onAwardSelect = {},
        onRetry = {},
        onTabClick = {},
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 1700)
@Composable
private fun AwardDetailPreviewSignature() {
    val award = Award(
        id = "5",
        slug = "signature-2025-creator",
        name = "Signature 2025 - Creator",
        shortDescription = "Short",
        imageDrawable = "award_signature_2025_creator",
        displayOrder = 5,
        longDescription = "Giải thưởng Signature vinh danh cá nhân hoặc tập thể thể hiện tinh thần đặc trưng mà Sun* hướng tới. Trong năm 2025, vinh danh Creator – cá nhân hoặc tập thể tiên phong trong hành động.",
        quantity = 1,
        quantityUnit = "Cá nhân hoặc tập thể",
        prizeValue = "5.000.000 VNĐ",
        prizeNote = "cho giải cá nhân",
        prizeValueSecondary = "8.000.000 VNĐ",
        prizeNoteSecondary = "cho giải tập thể",
    )
    AwardDetailScreen(
        state = AwardDetailUiState(
            awards = AwardsResult.Success(listOf(award)),
            selectedSlug = "signature-2025-creator",
        ),
        onLanguageSelect = {},
        onDropdownOpenChange = {},
        onAwardSelect = {},
        onRetry = {},
        onTabClick = {},
    )
}
