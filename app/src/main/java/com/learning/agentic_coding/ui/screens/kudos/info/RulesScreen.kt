package com.learning.agentic_coding.ui.screens.kudos.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.ui.screens.kudos.info.components.CollectibleIconRow
import com.learning.agentic_coding.ui.screens.kudos.info.components.HeroBadgePill
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoBackground
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoTopBar
import com.learning.agentic_coding.ui.screens.kudos.info.components.RulesActionBar

/**
 * "Thể lệ" — program rules page (MoMorph zIuFaHAid4). Reached from the info icon on the
 * Kudos home. Scrolling content (Hero tiers, collectible icons, Kudos Quốc Dân) above a
 * fixed Đóng / Viết Kudos action row.
 */
@Composable
fun RulesScreen(
    onClose: () -> Unit,
    onWriteKudos: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        InfoBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            InfoTopBar(title = stringResource(R.string.rules_topbar_title), onBack = onClose)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp, bottom = 20.dp),
            ) {
                Text(
                    text = stringResource(R.string.rules_section_title),
                    color = colorResource(R.color.saa_button_yellow),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(16.dp))
                GoldSubtitle(stringResource(R.string.rules_receiver_subtitle))
                Spacer(Modifier.height(10.dp))
                BodyText(stringResource(R.string.rules_receiver_desc))
                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                HeroTier(
                    imgRes = R.drawable.ic_new_hero,
                    count = stringResource(R.string.rules_hero_new_count),
                    desc = stringResource(R.string.rules_hero_desc_early),
                )
                HeroTier(
                    imgRes = R.drawable.ic_rising_hero,
                    count = stringResource(R.string.rules_hero_rising_count),
                    desc = stringResource(R.string.rules_hero_desc_early),
                )
                HeroTier(
                    imgRes = R.drawable.ic_super_hero,
                    count = stringResource(R.string.rules_hero_super_count),
                    desc = stringResource(R.string.rules_hero_desc_high),
                )
                HeroTier(
                    imgRes = R.drawable.ic_lendary_hero,
                    count = stringResource(R.string.rules_hero_legend_count),
                    desc = stringResource(R.string.rules_hero_desc_high),
                )

                Spacer(Modifier.height(8.dp))
                GoldSubtitle(stringResource(R.string.rules_sender_subtitle))
                Spacer(Modifier.height(10.dp))
                BodyText(stringResource(R.string.rules_sender_desc))
                Spacer(Modifier.height(16.dp))
                CollectibleIconRow()
                Spacer(Modifier.height(16.dp))
                BodyText(stringResource(R.string.rules_collect_conclusion))
                Spacer(Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.rules_national_title),
                    color = colorResource(R.color.saa_button_yellow),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(10.dp))
                BodyText(stringResource(R.string.rules_national_desc))
            }
            RulesActionBar(onClose = onClose, onWriteKudos = onWriteKudos)
        }
    }
}

@Composable
private fun GoldSubtitle(text: String) {
    Text(
        text = text,
        color = colorResource(R.color.saa_button_yellow),
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 21.sp,
    )
}

@Composable
private fun BodyText(text: String) {
    Text(
        text = text,
        color = colorResource(R.color.saa_text_secondary),
        fontSize = 14.sp,
        lineHeight = 21.sp,
    )
}

@Composable
private fun Divider() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(R.color.saa_divider))
    )
}

@Composable
private fun HeroTier(imgRes: Int, count: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp)
    ) {
        Image(
            painter = painterResource(imgRes),
            contentDescription = null,
            modifier = Modifier.size(98.dp, 32.dp),
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = count,
            color = colorResource(R.color.saa_text_primary),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(6.dp))
        BodyText(desc)
    }
}
