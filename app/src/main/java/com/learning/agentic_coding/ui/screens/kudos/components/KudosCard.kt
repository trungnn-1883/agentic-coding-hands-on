package com.learning.agentic_coding.ui.screens.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosParty
import com.learning.agentic_coding.domain.KudosPost

/**
 * Shared kudos post card (MoMorph node mms_B.3_KUDO - Highlight). Cream-yellow card with:
 * sender + receiver header, divider, timestamp + IDOL GIỚI TRẺ title, content in nested
 * cream panel, hashtags, divider, action footer (hearts + Copy Link + Xem chi tiết).
 *
 * Designed to be reused on KudosHome (highlight + recent) and AllKudos (full list).
 */
@Composable
fun KudosCard(
    post: KudosPost,
    modifier: Modifier = Modifier,
    onDetailClick: ((String) -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.saa_kudos_card_bg))
            .border(
                width = 1.dp,
                color = colorResource(R.color.saa_card_border),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        KudosCardHeader(sender = post.sender, receiver = post.receiver)
        ThinDivider()
        Text(
            text = KudosFormatters.cardTimestamp(post.postedAt),
            color = colorResource(R.color.saa_kudos_card_subtext),
            fontSize = 11.sp,
        )
        Text(
            text = post.title,
            color = colorResource(R.color.saa_kudos_idol_title),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        KudosCardBody(content = post.content, hashtags = post.hashtags)
        ThinDivider()
        KudosCardActions(
            hearts = post.hearts,
            kudoId = post.id,
            onDetailClick = onDetailClick?.let { { it(post.id) } },
        )
    }
}

@Composable
private fun KudosCardHeader(sender: KudosParty, receiver: KudosParty) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        PartyBlock(party = sender, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.size(width = 32.dp, height = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_paper_plane),
                contentDescription = null,
                tint = colorResource(R.color.saa_kudos_card_text),
                modifier = Modifier.size(18.dp),
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
        KudosAvatar(name = party.name, size = 32.dp, url = party.avatarUrl)
        Text(
            text = party.name,
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 12.sp,
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
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "·",
                color = colorResource(R.color.saa_kudos_card_subtext),
                fontSize = 10.sp,
            )
            KudosBadgePill(badge = party.badge)
        }
    }
}

@Composable
private fun KudosCardBody(content: String, hashtags: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.saa_kudos_card_bg))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = content,
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            modifier = Modifier.background(colorResource(R.color.saa_kudos_content_bg_40))
        )
        if (hashtags.isNotEmpty()) {
            Text(
                text = hashtags.joinToString(" "),
                color = colorResource(R.color.saa_kudos_hashtag_red),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun KudosCardActions(hearts: Int, kudoId: String, onDetailClick: (() -> Unit)?) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = formatHearts(hearts),
                color = colorResource(R.color.saa_kudos_card_text),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Icon(
                painter = painterResource(R.drawable.ic_heart_filled),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp),
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
            ActionLink(
                label = stringResource(R.string.kudos_card_details),
                iconRes = R.drawable.ic_send_arrow,
                onClick = onDetailClick,
            )
        }
    }
}

@Composable
private fun ActionLink(label: String, iconRes: Int, onClick: (() -> Unit)? = null) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = clickModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 12.sp,
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

private fun formatHearts(value: Int): String {
    if (value < 1000) return value.toString()
    val thousands = value / 1000
    val remainder = value % 1000
    return if (remainder == 0) "$thousands.000" else "$thousands.${
        remainder.toString().padStart(3, '0')
    }"
}
