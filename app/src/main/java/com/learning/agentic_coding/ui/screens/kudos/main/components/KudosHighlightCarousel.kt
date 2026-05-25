package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosPost
import com.learning.agentic_coding.ui.screens.kudos.components.KudosCard
import kotlinx.coroutines.launch

/**
 * Highlight carousel: dropdown filter row + horizontally-pageable cards + "current/total"
 * indicator. Arrow buttons step one card at a time; swipe also works via HorizontalPager.
 * Filter changes snap the pager back to page 0 so the user lands on the first match.
 */
@Composable
fun KudosHighlightCarousel(
    posts: List<KudosPost>,
    selectedDept: String?,
    selectedHashtag: String?,
    departmentOptions: List<String>,
    hashtagOptions: List<String>,
    onDeptSelect: (String?) -> Unit,
    onHashtagSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val filtered = posts.filter { post ->
        (selectedDept == null || post.sender.dept == selectedDept || post.receiver.dept == selectedDept) &&
            (selectedHashtag == null || post.hashtags.any { it.equals(selectedHashtag, ignoreCase = true) })
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KudosFilterDropdown(
                placeholder = stringResource(R.string.kudos_dropdown_hashtag),
                selected = selectedHashtag,
                options = hashtagOptions,
                onSelect = onHashtagSelect,
            )
            KudosFilterDropdown(
                placeholder = stringResource(R.string.kudos_dropdown_department),
                selected = selectedDept,
                options = departmentOptions,
                onSelect = onDeptSelect,
            )
        }
        if (filtered.isNotEmpty()) {
            HighlightPager(posts = filtered)
        } else {
            Text(
                text = "—",
                color = colorResource(R.color.saa_text_secondary),
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun HighlightPager(posts: List<KudosPost>) {
    val pagerState = rememberPagerState(pageCount = { posts.size })
    val scope = rememberCoroutineScope()

    // Reset to page 0 whenever the upstream list shrinks below current page (filter applied).
    LaunchedEffect(posts.size) {
        if (pagerState.currentPage >= posts.size) {
            pagerState.scrollToPage(0)
        }
    }

    val canPrev = pagerState.currentPage > 0
    val canNext = pagerState.currentPage < posts.size - 1

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ArrowButton(
                rotate = 0f,
                enabled = canPrev,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                },
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp),
                pageSpacing = 12.dp,
            ) { page ->
                KudosCard(post = posts[page])
            }
            ArrowButton(
                rotate = 180f,
                enabled = canNext,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                },
            )
        }
        Text(
            text = stringResource(
                R.string.kudos_pager_indicator,
                pagerState.currentPage + 1,
                posts.size,
            ),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ArrowButton(rotate: Float, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .alpha(if (enabled) 1f else 0.3f)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp).rotate(rotate + 90f),
        )
    }
}
