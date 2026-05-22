package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Kudos section — spec mms_5. Eyebrow header + banner image + "ĐIỂM MỚI"
 * badge + description + "Chi tiết" CTA. Caller decides whether to render
 * via the [visible] gate (TC_GUI_005 / FUN_009).
 */
@Composable
fun KudosSection(
    visible: Boolean,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!visible) return
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionEyebrow(
            eyebrow = stringResource(R.string.home_kudos_eyebrow),
            title = stringResource(R.string.home_kudos_title),
        )
        KudosBanner()
        KudosNewBadge()
        Text(
            text = stringResource(R.string.home_kudos_description),
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
            lineHeight = 22.sp,
        )
        KudosDetailsButton(onClick = onDetailsClick)
    }
}

@Composable
private fun KudosBanner() {
    Image(
        painter = painterResource(R.drawable.kudos_banner_full),
        contentDescription = stringResource(R.string.home_kudos_logo_text),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
private fun KudosNewBadge() {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = colorResource(R.color.saa_kudos_new_badge_text),
                shape = RoundedCornerShape(2.dp),
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = stringResource(R.string.home_kudos_new_badge),
            color = colorResource(R.color.saa_kudos_new_badge_text),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
private fun KudosDetailsButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.saa_button_yellow),
            contentColor = colorResource(R.color.saa_text_on_button),
        ),
        modifier = Modifier.width(120.dp).height(36.dp),
    ) {
        Text(
            text = stringResource(R.string.home_kudos_details),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_external),
            contentDescription = null,
            tint = colorResource(R.color.saa_text_on_button),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(14.dp),
        )
    }
}
