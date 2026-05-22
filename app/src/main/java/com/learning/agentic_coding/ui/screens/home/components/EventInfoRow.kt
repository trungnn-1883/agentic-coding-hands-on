package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Event metadata block under the countdown — spec mms_2 "event info":
 * "Thời gian: 26/06/2026", "Địa điểm: Âu Cơ Art Center", livestream note.
 */
@Composable
fun EventInfoBlock(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EventInfoLine(
            label = stringResource(R.string.home_event_time_label),
            value = stringResource(R.string.home_event_date),
        )
        EventInfoLine(
            label = stringResource(R.string.home_event_venue_label),
            value = stringResource(R.string.home_event_venue),
        )
        Text(
            text = stringResource(R.string.home_livestream_note),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 13.sp,
            lineHeight = 18.sp,
        )
    }
}

@Composable
private fun EventInfoLine(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp),  verticalAlignment = Alignment.Bottom,) {
        Text(
            text = label,
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 14.sp,
        )
        Text(
            text = value,
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
