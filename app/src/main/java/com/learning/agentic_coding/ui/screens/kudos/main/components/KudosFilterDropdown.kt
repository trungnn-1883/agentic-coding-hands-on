package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Pill-shaped dropdown used for hashtag + department filters on KudosHome
 * (mms_A_Dropdown-Hashtag / mms_A_Dropdown-CEV). Shows the placeholder when no
 * value is selected; first item in the menu is highlighted gold when selected.
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
    val labelColor = colorResource(R.color.saa_text_primary)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colorResource(R.color.saa_kudos_dropdown_bg))
            .clickable { expanded = !expanded }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(20.dp)
            .width(IntrinsicWidthHack),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = selected ?: placeholder,
            color = labelColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
        Icon(
            painter = painterResource(R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(start = 4.dp)
                .height(14.dp),
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .background(colorResource(R.color.saa_kudos_dropdown_bg)),
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
                        color = if (isSelected) {
                            colorResource(R.color.saa_kudos_dropdown_selected)
                        } else {
                            labelColor
                        },
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

// Use 132dp as a stable minimum width matching the Figma design (two filter pills sit
// side by side with a small gap). IntrinsicSize would expand to fit options.
private val IntrinsicWidthHack = 132.dp
