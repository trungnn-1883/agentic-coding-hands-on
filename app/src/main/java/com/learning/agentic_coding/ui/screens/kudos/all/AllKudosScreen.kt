package com.learning.agentic_coding.ui.screens.kudos.all

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.data.kudos.KudosResult
import com.learning.agentic_coding.ui.screens.home.components.HomeBottomNav
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.kudos.components.KudosCard
import com.learning.agentic_coding.ui.screens.kudos.components.KudosKvBackground
import com.learning.agentic_coding.ui.screens.kudos.components.KudosSectionHeading

/**
 * All Kudos detail screen — MoMorph frame j_a2GQWKDJ. Back-arrow top bar, ALL KUDOS heading,
 * scrollable list of kudos cards, Kudos tab active in bottom nav.
 */
@Composable
fun AllKudosScreen(
    state: AllKudosUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
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
                Column(modifier = Modifier.fillMaxSize()){
                    AllKudosTopBar(onBack = onBack)
                    AllKudosBody(
                        state = state,
                        onRetry = onRetry,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            HomeBottomNav(activeTab = HomeTab.KUDOS, onTabClick = onTabClick)
        }
    }
}

@Composable
private fun AllKudosTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.kudos_all_back_cd),
                tint = colorResource(R.color.saa_text_primary),
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = stringResource(R.string.kudos_all_screen_title),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(end = 40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

@Composable
private fun AllKudosBody(
    state: AllKudosUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (val data = state.data) {
        is KudosResult.Loading -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("...", color = colorResource(R.color.saa_text_secondary), fontSize = 14.sp)
        }
        is KudosResult.Error -> Column(
            modifier = modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = data.message, color = colorResource(R.color.saa_error), fontSize = 14.sp)
            Text(
                text = stringResource(R.string.home_awards_retry),
                color = colorResource(R.color.saa_button_yellow),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 12.dp).clickable(onClick = onRetry),
            )
        }
        is KudosResult.Success -> AllKudosList(posts = data.posts, modifier = modifier)
    }
}

@Composable
private fun AllKudosList(
    posts: List<com.learning.agentic_coding.domain.KudosPost>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            // Same KV image as Kudos home, behind the ALL KUDOS heading.
            Box {
                KudosKvBackground(heightDp = 200)
                Box(modifier = Modifier.padding(top = 16.dp)) {
                    KudosSectionHeading(title = stringResource(R.string.kudos_all_title))
                }
            }
        }
        items(posts, key = { it.id }) { post ->
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                KudosCard(post = post)
            }
        }
    }
}
