package com.learning.agentic_coding.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.Language

/**
 * Language selector from Figma node `6885:8976` (Login) / screen `uUvW6Qm1ve` (open list).
 *
 * Closed trigger: 90×32 dp pill with padding (4 top, 0 right, 4 bottom, 8 left), borderRadius 4.
 * The trigger has no own background — it sits on the header gradient overlay of the Login screen,
 * which provides the dark backdrop.
 *
 * Open menu: dark panel listing all [Language] entries, selected row highlighted.
 *
 * Stateless about persistence — caller owns the selection.
 */
@Composable
fun LanguageDropdown(
    selected: Language,
    onSelect: (Language) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val triggerShape = RoundedCornerShape(4.dp)

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(32.dp)
                .clip(triggerShape)
                .clickable { expanded = !expanded }
                .padding(start = 8.dp, top = 4.dp, end = 0.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            FlagIcon(language = selected)
            Text(
                text = selected.code,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_down),
                contentDescription = stringResource(R.string.language_dropdown_content_description),
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colorResource(R.color.saa_dropdown_bg)),
        ) {
            Language.entries.forEach { language ->
                val isSelected = language == selected
                DropdownMenuItem(
                    modifier = Modifier.background(
                        if (isSelected) colorResource(R.color.saa_dropdown_selected_bg) else Color.Transparent,
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            FlagIcon(language = language)
                            Text(
                                text = language.code,
                                color = Color.White,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = 14.sp,
                            )
                        }
                    },
                    onClick = {
                        onSelect(language)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun FlagIcon(language: Language) {
    Image(
        painter = painterResource(language.flagRes),
        contentDescription = null,
        modifier = Modifier.size(24.dp),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF00101A)
@Composable
private fun LanguageDropdownClosedPreview() {
    LanguageDropdown(selected = Language.VN, onSelect = {})
}

@Preview(showBackground = true, backgroundColor = 0xFF00101A, heightDp = 80)
@Composable
private fun LanguageDropdownAllPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Language.entries.forEach { LanguageDropdown(selected = it, onSelect = {}) }
    }
}
