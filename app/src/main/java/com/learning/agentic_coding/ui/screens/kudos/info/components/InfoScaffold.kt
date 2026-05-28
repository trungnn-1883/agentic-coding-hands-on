package com.learning.agentic_coding.ui.screens.kudos.info.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Full-screen "Keyvisual" background shared by the Kudos info pages (Tiêu chuẩn cộng đồng,
 * Thể lệ). Draws the colorful smoke art from Figma (saa_keyvisual_bg, node 6885:10862) over
 * the dark base, top-aligned with a bottom fade so the scrolling content stays readable.
 *
 * Sits as the first child of a Box, behind the scrolling content.
 */
@Composable
fun InfoBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Image(
            painter = painterResource(R.drawable.saa_keyvisual_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.9f),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            colorResource(R.color.saa_bg_dark).copy(alpha = 0.35f),
                            colorResource(R.color.saa_bg_dark).copy(alpha = 0.85f),
                        ),
                    ),
                ),
        )
    }
}

/** Top bar with a back arrow and a centered title — shared by both Kudos info pages. */
@Composable
fun InfoTopBar(title: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().height(48.dp).padding(horizontal = 12.dp),
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
            text = title,
            color = colorResource(R.color.saa_text_primary),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f).padding(end = 40.dp),
            textAlign = TextAlign.Center,
        )
    }
}
