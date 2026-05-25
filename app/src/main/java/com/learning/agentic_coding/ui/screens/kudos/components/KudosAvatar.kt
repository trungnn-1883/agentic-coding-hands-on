package com.learning.agentic_coding.ui.screens.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Placeholder avatar — colored circle with the person's initial. No remote image loading
 * is wired in this project, so we render a deterministic accent per name.
 */
@Composable
fun KudosAvatar(name: String, size: Dp = 32.dp) {
    val palette = listOf(
        R.color.saa_kudos_avatar_a,
        R.color.saa_kudos_avatar_b,
        R.color.saa_kudos_avatar_c,
        R.color.saa_kudos_avatar_d,
        R.color.saa_kudos_avatar_e,
        R.color.saa_kudos_avatar_f,
    )
    val bg = colorResource(palette[KudosFormatters.avatarColorIndex(name)])
    val initial = name.firstOrNull()?.uppercaseChar()?.toString().orEmpty()
    Box(
        modifier = Modifier.size(size).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = (size.value * 0.42f).sp,
        )
    }
}
