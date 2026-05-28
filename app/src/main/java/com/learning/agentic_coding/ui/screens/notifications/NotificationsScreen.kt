package com.learning.agentic_coding.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.data.notifications.NotificationsResult
import com.learning.agentic_coding.domain.Notification
import com.learning.agentic_coding.domain.NotificationType
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoBackground
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoTopBar
import com.learning.agentic_coding.ui.screens.notifications.components.MarkAllReadBar
import com.learning.agentic_coding.ui.screens.notifications.components.NotificationItemRow
import java.time.OffsetDateTime

/**
 * Notifications screen — MoMorph [iOS] Notifications (_b68CBWKl5). Back arrow + centered
 * title, "Mark all read" action bar, then a LazyColumn of [NotificationItemRow]s. No
 * bottom nav (matches Figma).
 */
@Composable
fun NotificationsScreen(
    state: NotificationsUiState,
    onBack: () -> Unit,
    onItemClick: (Notification) -> Unit,
    onCommunityStandardsClick: () -> Unit,
    onMarkAllRead: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Smoke-art keyvisual background matching the Figma design — same shared
        // background used by CommunityStandards / Rules info screens.
        InfoBackground()
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            InfoTopBar(
                title = stringResource(R.string.notifications_topbar_title),
                onBack = onBack,
            )
            val items = (state.data as? NotificationsResult.Success)?.items.orEmpty()
            val hasUnread = items.any { !it.isRead }
            MarkAllReadBar(enabled = hasUnread, onClick = onMarkAllRead)
            when (val data = state.data) {
                NotificationsResult.Loading -> EmptyMessage(stringResource(R.string.notifications_empty))
                is NotificationsResult.Error -> ErrorBlock(message = data.message, onRetry = onRetry)
                is NotificationsResult.Success ->
                    if (data.items.isEmpty()) EmptyMessage(stringResource(R.string.notifications_empty))
                    else NotificationList(
                        items = data.items,
                        onItemClick = onItemClick,
                        onCommunityStandardsClick = onCommunityStandardsClick,
                    )
            }
        }
    }
}

@Composable
private fun NotificationList(
    items: List<Notification>,
    onItemClick: (Notification) -> Unit,
    onCommunityStandardsClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        items(items, key = { it.id }) { item ->
            NotificationItemRow(
                notification = item,
                onClick = { onItemClick(item) },
                onCommunityStandardsClick = onCommunityStandardsClick,
            )
        }
    }
}

@Composable
private fun EmptyMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = 80.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            text = text,
            color = colorResource(R.color.saa_text_dim),
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun ErrorBlock(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 80.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.notifications_error),
            color = colorResource(R.color.saa_error),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = message, color = colorResource(R.color.saa_text_dim), fontSize = 12.sp)
        androidx.compose.material3.TextButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.notifications_retry),
                color = colorResource(R.color.saa_button_yellow),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun NotificationsScreenPreview() {
    val now = OffsetDateTime.now()
    NotificationsScreen(
        state = NotificationsUiState(
            data = NotificationsResult.Success(
                listOf(
                    Notification("1", NotificationType.KUDOS_RECEIVED,
                        "Sunner Huỳnh Dương Xuân Nhật vừa gửi đến bạn lời ghi nhận đầy yêu thương!",
                        null, false, now.minusMinutes(15), 1),
                    Notification("2", NotificationType.HEART_RECEIVED,
                        "Wow! Lời nhắn gửi của bạn cho Sunner Trần Minh Anh vừa nhận thêm lượt tim!",
                        null, true, now.minusHours(1), 2),
                    Notification("3", NotificationType.SECRET_BOX,
                        "Chúc mừng! Bạn vừa nhận được lượt mở Secret Box mới!",
                        null, true, now.minusDays(1), 3),
                    Notification("4", NotificationType.CONTENT_HIDDEN,
                        "Tiếc quá! Bạn có một lời nhắn bị tạm ẩn vì \"vướng\" một số tiêu chuẩn!",
                        null, true, now.minusDays(30), 5),
                ),
            ),
        ),
        onBack = {},
        onItemClick = {},
        onCommunityStandardsClick = {},
        onMarkAllRead = {},
        onRetry = {},
    )
}
