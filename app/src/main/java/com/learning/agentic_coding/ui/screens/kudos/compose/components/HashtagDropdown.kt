package com.learning.agentic_coding.ui.screens.kudos.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosHashtag

/**
 * Hashtag picker section (MoMorph aKWA2klsnt). Renders selected chips wrapped + a "+ Hashtag"
 * button; when [expanded] is true, the multi-select panel slides down underneath.
 */
@Composable
fun HashtagPicker(
    available: List<KudosHashtag>,
    selected: List<KudosHashtag>,
    maxCount: Int,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onToggleSelect: (KudosHashtag) -> Unit,
    onRemove: (KudosHashtag) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (selected.isNotEmpty()) {
            ChipFlow(items = selected, onRemove = onRemove)
        }
        AddHashtagButton(maxCount = maxCount, onClick = onToggleExpanded)
        if (expanded) {
            DropdownPanel(
                available = available,
                selected = selected,
                onToggleSelect = onToggleSelect,
            )
        }
    }
}

@Composable
private fun ChipFlow(items: List<KudosHashtag>, onRemove: (KudosHashtag) -> Unit) {
    // Simple 2-col flow: spread chips in a row; if more than 2 wrap to next row.
    val rows = items.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowItems.forEach { tag ->
                    SelectedChip(tag = tag, onRemove = { onRemove(tag) })
                }
            }
        }
    }
}

@Composable
private fun SelectedChip(tag: KudosHashtag, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(1.dp, colorResource(R.color.saa_kudos_card_divider), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = tag.tag,
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "×",
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 14.sp,
            modifier = Modifier.clickable(onClick = onRemove),
        )
    }
}

@Composable
private fun AddHashtagButton(maxCount: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(1.dp, colorResource(R.color.saa_kudos_card_divider), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "+ Hashtag",
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "(Tối đa $maxCount)",
            color = colorResource(R.color.saa_kudos_card_subtext),
            fontSize = 10.sp,
        )
    }
}

@Composable
private fun DropdownPanel(
    available: List<KudosHashtag>,
    selected: List<KudosHashtag>,
    onToggleSelect: (KudosHashtag) -> Unit,
) {
    val selectedIds = selected.map { it.id }.toSet()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.saa_kudos_pill_dark))
            .heightIn(max = 280.dp),
    ) {
        LazyColumn {
            items(available) { tag ->
                val isSelected = tag.id in selectedIds
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable { onToggleSelect(tag) }
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = tag.tag,
                        color = colorResource(R.color.saa_text_primary),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check_small),
                                contentDescription = null,
                                tint = colorResource(R.color.saa_kudos_pill_dark),
                                modifier = Modifier.size(12.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
