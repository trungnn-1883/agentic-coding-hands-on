package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Floating action button cluster — spec mms_6: a pill containing the pen icon
 * (open WriteKudo) and the Sun*-Kudos icon (open Kudos feed). Both clicks are
 * debounced 400ms to satisfy TC_FUN_013 (no duplicate navigation on rapid taps).
 */
@Composable
fun HomeFab(
    onPenClick: () -> Unit,
    onKudosClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val debouncedPen = rememberDebounced(onPenClick)
    val debouncedKudos = rememberDebounced(onKudosClick)
    Row(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(colorResource(R.color.saa_button_yellow))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        IconButton(onClick = debouncedPen, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_pen),
                contentDescription = stringResource(R.string.home_fab_pen_cd),
                tint = colorResource(R.color.saa_text_on_button),
                modifier = Modifier.size(24.dp),
            )
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp)
                .background(Color(0x33000000)),
        )
        IconButton(onClick = debouncedKudos, modifier = Modifier.size(40.dp)) {
            Text(
                text = "S",
                color = colorResource(R.color.saa_badge_red),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun rememberDebounced(action: () -> Unit, windowMs: Long = 400L): () -> Unit {
    val lastTap = remember { mutableLongStateOf(0L) }
    return {
        val now = System.currentTimeMillis()
        if (now - lastTap.longValue >= windowMs) {
            lastTap.longValue = now
            action()
        }
    }
}
