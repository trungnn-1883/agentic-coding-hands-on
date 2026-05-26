package com.learning.agentic_coding.ui.screens.kudos.detail

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.learning.agentic_coding.R
import com.learning.agentic_coding.data.kudos.KudoDetailResult
import com.learning.agentic_coding.domain.KudosParty
import com.learning.agentic_coding.domain.KudosPost
import com.learning.agentic_coding.ui.screens.home.components.HomeBottomNav
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.kudos.components.KudosAvatar
import com.learning.agentic_coding.ui.screens.kudos.components.KudosBadgePill
import com.learning.agentic_coding.ui.screens.kudos.components.KudosFormatters
import com.learning.agentic_coding.ui.screens.kudos.components.KudosKvBackground
import com.learning.agentic_coding.ui.screens.kudos.components.copyKudoLinkToClipboard

/**
 * Kudo detail screen — MoMorph frame T0TR16k0vH. KV background, "Kudo" top bar, single
 * cream card showing sender → arrow → receiver header with photo avatars, divider,
 * timestamp, centered uppercase title, body, attached-image strip (≤5), hashtags,
 * action bar (heart / Copy Link / Xem chi tiết). Kudos tab active in bottom nav.
 */
@Composable
fun KudoDetailScreen(
    state: KudoDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onHeartToggle: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .statusBarsPadding()) {
                KudosKvBackground(heightDp = 720)
                Column(modifier = Modifier.fillMaxSize()) {
                    KudoDetailTopBar(onBack = onBack)
                    KudoDetailBody(
                        state = state,
                        onRetry = onRetry,
                        onHeartToggle = onHeartToggle,
                    )
                }
            }
            HomeBottomNav(activeTab = HomeTab.KUDOS, onTabClick = onTabClick)
        }
    }
}

@Composable
private fun KudoDetailTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
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
        Text(
            text = stringResource(R.string.kudo_detail_title),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f).padding(end = 40.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun KudoDetailBody(
    state: KudoDetailUiState,
    onRetry: () -> Unit,
    onHeartToggle: () -> Unit,
) {
    when (val data = state.data) {
        is KudoDetailResult.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("...", color = colorResource(R.color.saa_text_secondary), fontSize = 14.sp)
        }
        is KudoDetailResult.Error -> Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
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
        is KudoDetailResult.Success -> KudoDetailCard(
            post = data.post,
            heartDelta = state.heartDelta,
            isLiked = state.isLiked,
            onHeartToggle = onHeartToggle,
        )
    }
}

@Composable
private fun KudoDetailCard(
    post: KudosPost,
    heartDelta: Int,
    isLiked: Boolean,
    onHeartToggle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(colorResource(R.color.saa_kudos_card_bg))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DetailHeader(sender = post.sender, receiver = post.receiver)
            ThinDivider()
            Text(
                text = KudosFormatters.cardTimestamp(post.postedAt),
                color = colorResource(R.color.saa_kudos_card_subtext),
                fontSize = 12.sp,
            )
            Text(
                text = post.title.uppercase(),
                color = colorResource(R.color.saa_kudos_idol_title),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = post.content,
                color = colorResource(R.color.saa_kudos_card_text),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .background(colorResource(R.color.saa_kudos_content_bg_40))
            )
            if (post.attachedImages.isNotEmpty()) {
                AttachedImageStrip(urls = post.attachedImages)
            }
            if (post.hashtags.isNotEmpty()) {
                Text(
                    text = post.hashtags.joinToString(" "),
                    color = colorResource(R.color.saa_kudos_hashtag_red),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            DetailActionBar(
                kudoId = post.id,
                hearts = post.hearts + heartDelta,
                isLiked = isLiked,
                onHeartToggle = onHeartToggle,
            )
        }
    }
}

@Composable
private fun DetailHeader(sender: KudosParty, receiver: KudosParty) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        PartyBlock(party = sender, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.size(width = 36.dp, height = 36.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_paper_plane),
                contentDescription = null,
                tint = colorResource(R.color.saa_kudos_card_text),
                modifier = Modifier.size(20.dp),
            )
        }
        PartyBlock(party = receiver, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PartyBlock(party: KudosParty, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        KudosAvatar(name = party.name, size = 40.dp, url = party.avatarUrl)
        Text(
            text = party.name,
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = party.dept,
                color = colorResource(R.color.saa_kudos_card_subtext),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            )
            KudosBadgePill(badge = party.badge)
        }
    }
}

@Composable
private fun AttachedImageStrip(urls: List<String>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(urls) { url ->
            SubcomposeAsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 58.dp, height = 58.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colorResource(R.color.saa_kudos_card_inner_bg)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun DetailActionBar(
    kudoId: String,
    hearts: Int,
    isLiked: Boolean,
    onHeartToggle: () -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onHeartToggle),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = hearts.toString(),
                color = colorResource(R.color.saa_kudos_card_text),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Icon(
                painter = painterResource(R.drawable.ic_heart_filled),
                contentDescription = null,
                tint = if (isLiked) colorResource(R.color.saa_kudos_hashtag_red) else Color.Unspecified,
                modifier = Modifier.size(18.dp),
            )
        }
        Box(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ActionLink(
                label = stringResource(R.string.kudos_card_copy_link),
                iconRes = R.drawable.ic_copy_link,
                onClick = { copyKudoLinkToClipboard(context, kudoId) },
            )
        }
    }
}

@Composable
private fun ActionLink(label: String, iconRes: Int, onClick: (() -> Unit)?) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = clickModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colorResource(R.color.saa_kudos_card_text),
            modifier = Modifier.size(14.dp),
        )
    }
}

@Composable
private fun ThinDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(R.color.saa_kudos_card_divider)),
    )
}

