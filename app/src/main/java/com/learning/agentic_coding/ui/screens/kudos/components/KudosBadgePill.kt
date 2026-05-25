package com.learning.agentic_coding.ui.screens.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosBadge

/**
 * Rising Hero / Legend Hero pill. Rising Hero = dark navy pill with gold text,
 * Legend Hero = gold pill with dark text (matches MoMorph card design).
 */
@Composable
fun KudosBadgePill(badge: KudosBadge, modifier: Modifier = Modifier) {
    val (bgRes, textColor) = when (badge) {
        KudosBadge.RisingHero ->
            R.color.saa_kudos_pill_dark to colorResource(R.color.saa_kudos_pill_gold)
        KudosBadge.LegendHero ->
            R.color.saa_kudos_pill_gold to colorResource(R.color.saa_kudos_card_text)
    }
    Text(
        text = stringResource(KudosFormatters.badgeLabelRes(badge)),
        color = textColor,
        fontSize = 9.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(colorResource(bgRes))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}

@Composable
internal fun deptDotColor(): Color = colorResource(R.color.saa_kudos_card_subtext)
