package com.learning.agentic_coding.ui.screens.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

/**
 * "Đánh dấu đọc tất cả" action row — spec mms_Button_read all (A.1). Icon + label,
 * left-aligned; subtle divider below. Disabled visually when `enabled` is false.
 */
@Composable
fun MarkAllReadBar(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tint = colorResource(R.color.saa_text_primary)
    val labelAlpha = if (enabled) 1f else 0.4f
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_format_list_numbered),
            contentDescription = null,
            tint = tint.copy(alpha = labelAlpha),
            modifier = Modifier.size(20.dp),
        )
        Box(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.notifications_mark_all_read),
            color = tint.copy(alpha = labelAlpha),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(R.color.saa_divider)),
    )
}
