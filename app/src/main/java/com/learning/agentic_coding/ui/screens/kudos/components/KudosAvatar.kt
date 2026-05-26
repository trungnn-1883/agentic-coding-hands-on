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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.learning.agentic_coding.R

/**
 * Circular avatar. When [url] is non-null, loads the remote image via Coil; otherwise (or
 * while loading / on error) falls back to a deterministic initial-letter chip.
 */
@Composable
fun KudosAvatar(name: String, size: Dp = 32.dp, url: String? = null) {
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
        if (url.isNullOrBlank()) {
            InitialChip(initial = initial, size = size)
        } else {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(size).clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = { InitialChip(initial = initial, size = size) },
                error = { InitialChip(initial = initial, size = size) },
            )
        }
    }
}

@Composable
private fun InitialChip(initial: String, size: Dp) {
    Text(
        text = initial,
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
        fontSize = (size.value * 0.42f).sp,
    )
}
