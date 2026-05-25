package com.learning.agentic_coding.ui.screens.kudos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.learning.agentic_coding.R

/**
 * Colorful "Keyvisual" background — the 375×723 image from Figma node 6891:15997
 * (orange/purple/blue smoke). Drawn as the topmost layer behind the header and hero,
 * with a vertical fade so the rest of the screen blends into the dark base.
 *
 * Place this inside a Box BEFORE the content Column so it sits underneath.
 */
@Composable
fun KudosKvBackground(modifier: Modifier = Modifier, heightDp: Int = 480) {
    Box(modifier = modifier.fillMaxWidth().height(heightDp.dp)) {
        Image(
            painter = painterResource(R.drawable.kudos_kv_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.85f),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
        )
        // Bottom fade to dark base so content below transitions smoothly.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            colorResource(R.color.saa_bg_dark).copy(alpha = 0.5f),
                            colorResource(R.color.saa_bg_dark),
                        ),
                    ),
                ),
        )
    }
}
