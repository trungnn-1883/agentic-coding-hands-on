package com.learning.agentic_coding.ui.screens.award.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.Award
import com.learning.agentic_coding.domain.Language

/**
 * Award info block — spec mms_2.3 (Figma node 6885:10292).
 *
 * Layout (per Figma node tree):
 *   container: column, gap 16dp, width 335dp
 *     - badge image (260dp art frame with circular medal)
 *     - title "award" block: column, gap 12dp
 *         - title row: icon 24dp + name (14sp/700/gold #FFEA9E)
 *         - description (14sp/300 Light/white, line 20sp)
 *     - divider 1dp #2E3940
 *     - quantity "award" block: column, gap 12dp
 *         - title row: diamond icon 24dp + "Số lượng giải thưởng" (14sp/700/gold)
 *         - number row: "10" (18sp/700/white) + " Cá nhân" (14sp/300/white)
 *     - divider
 *     - prize "award" block: column, gap 12dp
 *         - title row: flag icon 24dp + "Giá trị giải thưởng" (14sp/700/gold)
 *         - number row: "7.000.000 VNĐ" (18sp/700/WHITE — not gold!) + " cho mỗi…" (14sp/300/white)
 *
 * Critical Figma rules: labels are gold, values are WHITE (not gold). Divider is
 * dark blue-grey, not semi-transparent white.
 *
 * Dynamic text (description, quantity_unit, prize_note) is locale-resolved via
 * [Award.longDescriptionFor] / [Award.quantityUnitFor] / [Award.prizeNoteFor]
 * using the [language] passed from the screen state.
 */
@Composable
fun AwardInfoBlock(
    award: Award,
    language: Language,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AwardBadge(drawableName = award.imageDrawable)
        TitleAwardBlock(
            name = award.nameFor(language),
            description = award.longDescriptionFor(language),
        )
        HairlineDivider()
        QuantityAwardBlock(
            quantity = award.quantity,
            unit = award.quantityUnitFor(language),
        )
        HairlineDivider()
        PrizeAwardBlock(
            value = award.prizeValue,
            note = award.prizeNoteFor(language),
        )
        if (award.prizeValueSecondary != null) {
            HairlineDivider()
            PrizeAwardBlock(
                value = award.prizeValueSecondary,
                note = award.prizeNoteSecondaryFor(language).orEmpty(),
            )
        }
    }
}

/** Title award block — name title row + long description (Figma frame 6885:10294). */
@Composable
private fun TitleAwardBlock(name: String, description: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatTitleRow(iconRes = R.drawable.ic_award_target, label = name)
        Text(
            text = description,
            color = colorResource(R.color.saa_text_primary),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Light,
        )
    }
}

/** Quantity award block — label row + number row (Figma frame 6885:10300). */
@Composable
private fun QuantityAwardBlock(quantity: Int, unit: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatTitleRow(
            iconRes = R.drawable.ic_award_badge,
            label = stringResource(R.string.award_detail_quantity_label),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = String.format("%02d", quantity),
                color = colorResource(R.color.saa_text_primary),
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = unit,
                color = colorResource(R.color.saa_text_primary),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

/** Prize award block — label row + amount/note row (Figma frame 6885:10308). */
@Composable
private fun PrizeAwardBlock(value: String, note: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatTitleRow(
            iconRes = R.drawable.ic_award_trophy,
            label = stringResource(R.string.award_detail_value_label),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = value,
                color = colorResource(R.color.saa_text_primary),
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            if (note.isNotBlank()) {
                Text(
                    text = note,
                    color = colorResource(R.color.saa_text_primary),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }
}

/** Shared title row: 24dp icon + gold 14sp bold label, gap 8dp. */
@Composable
private fun StatTitleRow(iconRes: Int, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colorResource(R.color.saa_award_label_gold),
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = label,
            color = colorResource(R.color.saa_award_label_gold),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun HairlineDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(R.color.saa_award_divider)),
    )
}
