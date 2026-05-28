package com.learning.agentic_coding.ui.screens.kudos.info.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Hero-tier badge pill (New / Rising / Super / Legend Hero). Matches the Figma design
 * (node 6885:8945 variants, verified against the New Hero raster): a compact, fully-rounded
 * pill — dark-navy fill, thin gold (#FFEA9E) border, a green "Root Further" leaf motif
 * spilling off the left edge, and two-tone text (tier word bold white + "Hero" regular
 * dim-white). Only New Hero is rasterized in Figma (110×20, too low-res to upscale cleanly);
 * all four are reconstructed crisply to the same spec. [label] is the full "<Tier> Hero".
 */
@Composable
fun HeroBadgePill(label: String, modifier: Modifier = Modifier) {
    val tier = label.substringBeforeLast(' ')
    val suffix = label.substringAfterLast(' ')
    val pill = RoundedCornerShape(percent = 50)
    Row(
        modifier = modifier
            .clip(pill)
            .background(colorResource(R.color.saa_bg_dark))
            .border(1.dp, colorResource(R.color.saa_award_label_gold), pill)
            .padding(start = 4.dp, end = 14.dp, top = 3.dp, bottom = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_new_hero),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = tier,
            color = colorResource(R.color.saa_text_primary),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = suffix,
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

/** A single collectible-icon column: circular icon image + tiny centered label below. */
@Composable
fun CollectibleIconItem(iconRes: Int, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(40.dp).clip(CircleShape),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 8.sp,
            lineHeight = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }
}

/** Row of all six collectible icons, equally weighted across the width. */
@Composable
fun CollectibleIconRow(modifier: Modifier = Modifier) {
    val icons = listOf(
        R.drawable.saa_icon_revival to R.string.rules_icon_revival,
        R.drawable.saa_icon_touch_of_light to R.string.rules_icon_touch_of_light,
        R.drawable.saa_icon_stay_gold to R.string.rules_icon_stay_gold,
        R.drawable.saa_icon_flow_to_horizon to R.string.rules_icon_flow_to_horizon,
        R.drawable.saa_icon_beyond_boundary to R.string.rules_icon_beyond_boundary,
        R.drawable.saa_icon_root_further to R.string.rules_icon_root_further,
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        icons.forEach { (icon, label) ->
            CollectibleIconItem(
                iconRes = icon,
                label = stringResource(label),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** Bottom action bar for the Rules page: "Đóng" (outlined) + "Viết Kudos" (filled gold). */
@Composable
fun RulesActionBar(onClose: () -> Unit, onWriteKudos: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(R.color.saa_kudos_pill_dark))
                .border(1.dp, colorResource(R.color.saa_card_border), RoundedCornerShape(10.dp))
                .clickable(onClick = onClose),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.rules_btn_close),
                color = colorResource(R.color.saa_text_primary),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(R.color.saa_button_yellow))
                .clickable(onClick = onWriteKudos),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.rules_btn_write),
                color = colorResource(R.color.saa_text_on_button),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
