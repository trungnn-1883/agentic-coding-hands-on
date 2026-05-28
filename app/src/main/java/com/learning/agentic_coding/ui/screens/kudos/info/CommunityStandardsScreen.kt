package com.learning.agentic_coding.ui.screens.kudos.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoBackground
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoTopBar

/**
 * "Tiêu chuẩn cộng đồng" — static Community + Security standards page (MoMorph xms7csmDhD).
 * Reached from the Kudo compose screen. Back arrow only; no bottom nav.
 */
@Composable
fun CommunityStandardsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val rules = listOf(
        R.string.cs_rule_1, R.string.cs_rule_2, R.string.cs_rule_3, R.string.cs_rule_4,
        R.string.cs_rule_5, R.string.cs_rule_6, R.string.cs_rule_7, R.string.cs_rule_8,
        R.string.cs_rule_9, R.string.cs_rule_10,
    )
    Box(modifier = modifier.fillMaxSize()) {
        InfoBackground()
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            InfoTopBar(title = stringResource(R.string.cs_topbar_title), onBack = onBack)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp, bottom = 32.dp),
            ) {
                RootFurtherLogo()
                Spacer(Modifier.height(20.dp))

                SectionHeading(text = stringResource(R.string.cs_community_title))
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.cs_community_intro),
                    color = colorResource(R.color.saa_text_primary),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.cs_community_warning),
                    color = colorResource(R.color.saa_text_secondary),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                )
                Spacer(Modifier.height(14.dp))
                rules.forEachIndexed { index, res ->
                    NumberedItem(number = index + 1, text = stringResource(res))
                }

                Spacer(Modifier.height(20.dp))
                Box(
                    Modifier.fillMaxWidth().height(1.dp).background(colorResource(R.color.saa_divider)),
                )
                Spacer(Modifier.height(20.dp))

                SectionHeading(text = stringResource(R.string.cs_security_title))
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.cs_security_intro),
                    color = colorResource(R.color.saa_text_primary),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                )
                Spacer(Modifier.height(12.dp))
                BulletItem(text = stringResource(R.string.cs_security_bullet1))
                BulletItem(text = stringResource(R.string.cs_security_bullet2))
                Spacer(Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.cs_security_support),
                    color = colorResource(R.color.saa_button_yellow),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                )
            }
        }
    }
}

@Composable
private fun RootFurtherLogo() {
    Text(
        text = stringResource(R.string.cs_logo_line1) + "\n" + stringResource(R.string.cs_logo_line2),
        color = colorResource(R.color.saa_kudos_logo),
        fontSize = 34.sp,
        lineHeight = 34.sp,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
    )
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        color = colorResource(R.color.saa_button_yellow),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun NumberedItem(number: Int, text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.Top) {
        Text(
            text = "$number.",
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.width(26.dp).padding(end = 6.dp),
        )
        Text(
            text = text,
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun BulletItem(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.Top) {
        Text(
            text = "•",
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            modifier = Modifier.width(18.dp),
        )
        Text(
            text = text,
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            modifier = Modifier.weight(1f),
        )
    }
}
