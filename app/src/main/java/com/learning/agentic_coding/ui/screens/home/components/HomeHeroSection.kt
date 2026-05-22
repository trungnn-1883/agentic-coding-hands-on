package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.ui.screens.home.CountdownState

/**
 * Hero section — spec mms_2: keyvisual background + ROOT FURTHER logo +
 * countdown + event info + ABOUT AWARD / ABOUT KUDOS CTAs. All inside a
 * fixed-height Box so the keyvisual sits behind the content.
 */
@Composable
fun HomeHeroSection(
    countdown: CountdownState,
    onAboutAwardClick: () -> Unit,
    onAboutKudosClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        Image(
            painter = painterResource(R.drawable.bg_keyvisual),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
        )
        Box(modifier = Modifier.matchParentSize().background(heroDimGradient()))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.img_root_further),
                contentDescription = stringResource(R.string.login_root_further),
                modifier = Modifier.size(width = 247.dp, height = 109.dp),
            )
            CountdownTimer(state = countdown)
            EventInfoBlock()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HeroCta(
                    label = stringResource(R.string.home_about_award),
                    onClick = onAboutAwardClick,
                )
                HeroCta(
                    label = stringResource(R.string.home_about_kudos),
                    onClick = onAboutKudosClick,
                )
            }
        }
    }
}

@Composable
private fun HeroCta(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.saa_button_yellow),
            contentColor = colorResource(R.color.saa_text_on_button),
        ),
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_external),
            contentDescription = null,
            tint = colorResource(R.color.saa_text_on_button),
            modifier = Modifier.size(16.dp),
        )
    }
}

private fun heroDimGradient(): Brush = Brush.horizontalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF00101A),
        0.35f to Color(0xCC00101A),
        0.7f to Color(0x6600101A),
        1.0f to Color(0x0000101A),
    ),
)
