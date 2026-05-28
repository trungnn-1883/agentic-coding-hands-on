package com.learning.agentic_coding.ui.screens.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.Notification
import com.learning.agentic_coding.domain.NotificationType

/**
 * One row in the Notifications list — spec B.1. Icon (B.1.1) left, content + relative
 * time (B.1.2) middle, unread red dot (B.1.3) top-right. Bottom divider stitches rows
 * together. Tapping the row invokes [onClick]; the inline "Tiêu chuẩn cộng đồng" link
 * (CONTENT_HIDDEN type only) invokes [onCommunityStandardsClick].
 */
@Composable
fun NotificationItemRow(
    notification: Notification,
    onClick: () -> Unit,
    onCommunityStandardsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val style = notification.type.style()
    val resources = LocalContext.current.resources
    val relativeTime = formatRelativeTime(resources, notification.createdAt)
    val contentAlpha = if (notification.isRead) 0.75f else 1f
    val textWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(style.iconRes),
                contentDescription = stringResource(R.string.notifications_icon_cd),
                tint = colorResource(style.tintColorRes),
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = notification.content,
                    color = colorResource(R.color.saa_text_primary).copy(alpha = contentAlpha),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = textWeight,
                )
                if (notification.type == NotificationType.CONTENT_HIDDEN) {
                    InlineCommunityStandardsLink(onClick = onCommunityStandardsClick)
                }
                Text(
                    text = relativeTime,
                    color = colorResource(R.color.saa_text_dim),
                    fontSize = 12.sp,
                )
            }
            // Reserve right-edge space for the unread dot.
            Box(modifier = Modifier.width(12.dp))
        }
        if (!notification.isRead) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.saa_badge_red)),
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.saa_divider)),
        )
    }
}

@Composable
private fun InlineCommunityStandardsLink(onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(R.string.notifications_inline_community_standards),
            color = colorResource(R.color.saa_notif_inline_link),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_external),
            contentDescription = null,
            tint = colorResource(R.color.saa_notif_inline_link),
            modifier = Modifier.size(14.dp),
        )
    }
}
