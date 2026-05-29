package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosUserStats

/** ALL KUDOS stats card with five rows + Mở Secret Box CTA (mms_5/6 region). */
@Composable
fun KudosStatsCard(stats: KudosUserStats, onOpenSecretBox: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = colorResource(R.color.saa_kudos_stats_border),
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatsRow(label = stringResource(R.string.kudos_stats_received), value = stats.kudosReceived.toString())
        StatsRow(label = stringResource(R.string.kudos_stats_sent), value = stats.kudosSent.toString())
        HeartsRow(value = stats.heartsReceived, multiplier = stats.heartsMultiplier)
        StatsRow(label = stringResource(R.string.kudos_stats_secret_opened), value = stats.secretBoxOpened.toString())
        StatsRow(label = stringResource(R.string.kudos_stats_secret_unopened), value = stats.secretBoxUnopened.toString())
        OpenSecretBoxButton(onClick = onOpenSecretBox)
    }
}

@Composable
private fun StatsRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            color = colorResource(R.color.saa_text_primary),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value,
            color = colorResource(R.color.saa_award_label_gold),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun HeartsRow(value: Int, multiplier: Int) {
    // Figma mms_D.1.4: label (left) — sticker (in the empty middle) — value (right).
    // Wrapping the sticker in a weighted, centered Box lets it sit visually in the
    // gap, not glued to the number.
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.kudos_stats_hearts),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            HeartsMultiplierPill(multiplier = multiplier)
        }
        Text(
            text = value.toString(),
            color = colorResource(R.color.saa_award_label_gold),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun HeartsMultiplierPill(multiplier: Int) {
    // Figma mms_S_Group 435: organic flame sticker w/ "x2" overlay. Enlarged to
    // 32×38dp and recolored to warm orange (saa_gradient_orange) to match Figma
    // — the raster sticker is orange, not the red rounded pill we had before.
    // The "x2" sits centered with a black stroke layer behind a white fill copy.
    Box(
        modifier = Modifier.size(width = 32.dp, height = 38.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_flame),
            contentDescription = null,
            tint = colorResource(R.color.saa_gradient_orange),
            modifier = Modifier.size(width = 32.dp, height = 38.dp),
        )
        val text = stringResource(R.string.kudos_stats_multiplier, multiplier)
        // Black outline drawn first (slightly wider stroke for the larger sticker).
        Text(
            text = text,
            style = TextStyle(
                color = colorResource(R.color.saa_text_on_button),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                drawStyle = Stroke(width = 3f),
            ),
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun OpenSecretBoxButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.saa_button_yellow),
            contentColor = colorResource(R.color.saa_text_on_button),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
    ) {
        Text(
            text = stringResource(R.string.kudos_open_secret_box),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
        Icon(
            painter = painterResource(R.drawable.ic_gift),
            contentDescription = null,
            tint = colorResource(R.color.saa_text_on_button),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(16.dp),
        )
    }
}
