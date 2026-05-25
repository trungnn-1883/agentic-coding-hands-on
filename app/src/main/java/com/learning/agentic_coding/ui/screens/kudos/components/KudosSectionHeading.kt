package com.learning.agentic_coding.ui.screens.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * "Sun* Annual Awards 2025" eyebrow + thin gold divider + gold-typography section title.
 * The divider line is part of the Figma header instance, sitting between the eyebrow
 * and the section title (HIGHLIGHT KUDOS / SPOTLIGHT BOARD / ALL KUDOS).
 */
@Composable
fun KudosSectionHeading(title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.kudos_event_label),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.saa_kudos_stats_border).copy(alpha = 0.5f)),
        )
        Text(
            text = title,
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp,
        )
    }
}
