package com.learning.agentic_coding.ui.screens.kudos.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosRecipient
import com.learning.agentic_coding.ui.screens.home.components.HomeBottomNav
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.kudos.components.KudosKvBackground
import com.learning.agentic_coding.ui.screens.kudos.search.components.SunnerListItem

/**
 * Sunner search screen (MoMorph 3jgwke3E8O default + hldqjHoSRH active). Back arrow + search
 * field on the KV background. Blank query → Recent section; typed query → live results or
 * the empty-state message. Bottom nav stays visible with no tab active.
 */
@Composable
fun SunnerSearchScreen(
    state: SunnerSearchUiState,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onResultClick: (KudosRecipient) -> Unit,
    onRemoveRecent: (String) -> Unit,
    onToggleShowAllRecents: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f).statusBarsPadding()) {
                KudosKvBackground(heightDp = 720)
                Column(modifier = Modifier.fillMaxSize()) {
                    SearchBar(query = state.query, onQueryChange = onQueryChange, onBack = onBack)
                    if (state.isQueryActive) {
                        ResultsList(state = state, onResultClick = onResultClick)
                    } else {
                        RecentsList(
                            state = state,
                            onResultClick = onResultClick,
                            onRemoveRecent = onRemoveRecent,
                            onToggleShowAllRecents = onToggleShowAllRecents,
                        )
                    }
                }
            }
            HomeBottomNav(activeTab = HomeTab.KUDOS, onTabClick = onTabClick)
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier.size(40.dp).clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.kudos_all_back_cd),
                tint = colorResource(R.color.saa_text_primary),
                modifier = Modifier.size(24.dp),
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.saa_kudos_dropdown_bg))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(color = colorResource(R.color.saa_text_primary), fontSize = 14.sp),
                cursorBrush = SolidColor(colorResource(R.color.saa_text_primary)),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.sunner_search_hint),
                            color = colorResource(R.color.saa_text_dim),
                            fontSize = 14.sp,
                        )
                    }
                    inner()
                },
            )
        }
    }
}

@Composable
private fun ResultsList(state: SunnerSearchUiState, onResultClick: (KudosRecipient) -> Unit) {
    if (state.showEmptyState) {
        EmptyState()
        return
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.results, key = { it.id }) { sunner ->
            SunnerListItem(sunner = sunner, onClick = { onResultClick(sunner) })
        }
    }
}

@Composable
private fun RecentsList(
    state: SunnerSearchUiState,
    onResultClick: (KudosRecipient) -> Unit,
    onRemoveRecent: (String) -> Unit,
    onToggleShowAllRecents: () -> Unit,
) {
    if (state.recents.isEmpty()) return
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.sunner_search_recent),
                    color = colorResource(R.color.saa_text_primary),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                if (state.recents.size > SunnerSearchUiState.DEFAULT_RECENT_VISIBLE) {
                    Text(
                        text = stringResource(R.string.sunner_search_view_all),
                        color = colorResource(R.color.saa_text_primary),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable(onClick = onToggleShowAllRecents),
                    )
                }
            }
        }
        items(state.visibleRecents, key = { it.id }) { sunner ->
            SunnerListItem(
                sunner = sunner,
                onClick = { onResultClick(sunner) },
                onRemove = { onRemoveRecent(sunner.id) },
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = 48.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            text = stringResource(R.string.sunner_search_empty),
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
        )
    }
}
