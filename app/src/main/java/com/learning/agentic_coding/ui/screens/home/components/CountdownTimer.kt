package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.ui.screens.home.CountdownState

/**
 * Countdown block — spec mms_2: "Coming soon" + 3 tile columns (DAYS/HOURS/MINUTES)
 * each rendered as two digit tiles. When [state.isEnded] both the eyebrow and the
 * tile block are replaced by an "Event ended" label (per clarifications.md).
 */
@Composable
fun CountdownTimer(state: CountdownState, modifier: Modifier = Modifier) {
    if (state.isEnded) {
        Text(
            text = stringResource(R.string.home_event_ended),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier,
        )
        return
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        Text(
            text = stringResource(R.string.home_coming_soon),
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TimeUnit(value = state.days, label = stringResource(R.string.home_days))
            TimeUnit(value = state.hours, label = stringResource(R.string.home_hours))
            TimeUnit(value = state.minutes, label = stringResource(R.string.home_minutes))
        }
    }
}

@Composable
private fun TimeUnit(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val padded = value.coerceAtLeast(0).coerceAtMost(99).toString().padStart(2, '0')
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            DigitTile(padded[0])
            DigitTile(padded[1])
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = colorResource(R.color.saa_text_primary),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
private fun DigitTile(digit: Char) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 44.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(colorResource(R.color.saa_countdown_tile_bg))
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = digit.toString(),
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
