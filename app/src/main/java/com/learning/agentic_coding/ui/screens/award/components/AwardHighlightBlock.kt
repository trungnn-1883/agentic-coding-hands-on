package com.learning.agentic_coding.ui.screens.award.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.learning.agentic_coding.domain.Award
import com.learning.agentic_coding.domain.Language

/**
 * Highlight block — spec mms_B_Highlight. Eyebrow + section title + dropdown
 * trigger that opens an inline menu of every award (TC_FUN_005 / _006).
 */
@Composable
fun AwardHighlightBlock(
    selectedSlug: String,
    language: Language,
    awards: List<Award>,
    dropdownOpen: Boolean,
    onDropdownOpenChange: (Boolean) -> Unit,
    onAwardSelect: (Award) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedName = awards.firstOrNull { it.slug == selectedSlug }?.nameFor(language).orEmpty()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.home_awards_event_label),
            color = colorResource(R.color.saa_text_dim),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = stringResource(R.string.award_detail_system_title),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
        )
        DropdownTrigger(
            label = selectedName,
            isOpen = dropdownOpen,
            onClick = { onDropdownOpenChange(!dropdownOpen) },
        )
        AwardSelectionMenu(
            expanded = dropdownOpen,
            awards = awards,
            language = language,
            selectedSlug = selectedSlug,
            onDismiss = { onDropdownOpenChange(false) },
            onSelect = onAwardSelect,
        )
    }
}

@Composable
private fun DropdownTrigger(label: String, isOpen: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = colorResource(R.color.saa_button_yellow),
                shape = RoundedCornerShape(4.dp),
            )
            .background(colorResource(R.color.saa_dropdown_bg))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            color = colorResource(R.color.saa_text_primary),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Icon(
            painter = painterResource(R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = colorResource(R.color.saa_button_yellow),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun AwardSelectionMenu(
    expanded: Boolean,
    awards: List<Award>,
    language: Language,
    selectedSlug: String,
    onDismiss: () -> Unit,
    onSelect: (Award) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .background(colorResource(R.color.saa_dropdown_bg)),
    ) {
        awards.forEach { award ->
            val isSelected = award.slug == selectedSlug
            DropdownMenuItem(
                text = {
                    Text(
                        text = award.nameFor(language),
                        color = if (isSelected) {
                            colorResource(R.color.saa_button_yellow)
                        } else {
                            colorResource(R.color.saa_text_primary)
                        },
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    )
                },
                onClick = { onSelect(award) },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = if (isSelected) {
                    Modifier.background(colorResource(R.color.saa_dropdown_selected_bg))
                } else {
                    Modifier
                },
            )
        }
    }
}
