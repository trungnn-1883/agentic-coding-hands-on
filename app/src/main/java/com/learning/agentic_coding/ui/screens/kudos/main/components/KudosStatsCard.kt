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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.kudos_stats_hearts),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HeartsMultiplierPill(multiplier = multiplier)
            Text(
                text = value.toString(),
                color = colorResource(R.color.saa_award_label_gold),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun HeartsMultiplierPill(multiplier: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.saa_kudos_hashtag_red))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_flame),
                contentDescription = null,
                tint = colorResource(R.color.saa_button_yellow),
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = stringResource(R.string.kudos_stats_multiplier, multiplier),
                color = colorResource(R.color.saa_text_primary),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
            )
        }
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
