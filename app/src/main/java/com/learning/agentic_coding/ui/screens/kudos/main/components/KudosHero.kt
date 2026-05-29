package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
 * KUDOS hero block. Eyebrow ("Hệ thống ghi nhận và cảm ơn") sits above the big
 * KUDOS wordmark with the red flame swoosh (Figma node 6891:20498). The compose-prompt
 * pill sits below as a translucent dark surface with a pencil icon.
 *
 * The hero composable lays out content only — the colorful KV background is rendered
 * by [com.learning.agentic_coding.ui.screens.kudos.components.KudosKvBackground] one
 * layer up, so this block can sit on top transparently.
 */
@Composable
fun KudosHero(modifier: Modifier = Modifier, onComposeClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = stringResource(R.string.kudos_home_eyebrow),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
        KudosWordmark()
        ComposePromptCard(onClick = onComposeClick)
    }
}

@Composable
private fun KudosWordmark() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.ic_kudos_flame),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(width = 56.dp, height = 44.dp)
                .padding(end = 8.dp),
        )
        Text(
            text = stringResource(R.string.kudos_logo_text),
            color = colorResource(R.color.saa_kudos_logo),
            fontSize = 56.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-1.5).sp,
        )
    }
}

@Composable
private fun ComposePromptCard(onClick: () -> Unit) {
    // Figma mms_A.1: 335×40 pill, 1dp olive border (#998C5F) over yellow-tint 10% fill,
    // 4dp radius. Pencil icon + prompt text left-aligned, 8dp gap, 10dp inner padding.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(colorResource(R.color.saa_kudos_yellow_tint_10))
            .border(
                width = 1.dp,
                color = colorResource(R.color.saa_kudos_border_olive),
                shape = RoundedCornerShape(4.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_pen),
            contentDescription = null,
            tint = colorResource(R.color.saa_text_primary),
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = stringResource(R.string.kudos_compose_prompt),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
