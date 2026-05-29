package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Pill-shaped dropdown for hashtag + department filters on KudosHome
 * (Figma fO0Kt19sZZ mms_A_Dropdown). 129×40dp pill, 1dp olive (#998C5F) border
 * over yellow-tint 10% fill, 4dp radius. When a value is selected, the trigger
 * text turns gold; the matching menu row is also highlighted gold.
 */
@Composable
fun KudosFilterDropdown(
    placeholder: String,
    selected: String?,
    options: List<String>,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val defaultColor = colorResource(R.color.saa_text_primary)
    val selectedColor = colorResource(R.color.saa_kudos_dropdown_selected)
    // Box wraps trigger + menu together so the popup anchors to THIS trigger.
    // Without the wrapper, both filter triggers share a parent Row scope and the
    // department menu would drop down under the hashtag trigger.
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .width(DropdownWidth)
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(colorResource(R.color.saa_kudos_yellow_tint_10))
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.saa_kudos_border_olive),
                    shape = RoundedCornerShape(4.dp),
                )
                .clickable { expanded = !expanded }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = selected ?: placeholder,
                color = if (selected != null) selectedColor else defaultColor,
                fontSize = 13.sp,
                fontWeight = if (selected != null) FontWeight.SemiBold else FontWeight.Medium,
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_down),
                contentDescription = null,
                tint = if (selected != null) selectedColor else defaultColor,
                modifier = Modifier.height(14.dp),
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colorResource(R.color.saa_kudos_dropdown_bg)),
        ) {
            if (selected != null) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "—",
                            color = colorResource(R.color.saa_text_secondary),
                            fontSize = 14.sp,
                        )
                    },
                    onClick = {
                        onSelect(null)
                        expanded = false
                    },
                )
            }
            options.forEach { option ->
                val isSelected = option == selected
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (isSelected) selectedColor else defaultColor,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

// 129dp matches Figma; two pills + 8dp gap fit within the 335dp content column.
private val DropdownWidth = 129.dp
