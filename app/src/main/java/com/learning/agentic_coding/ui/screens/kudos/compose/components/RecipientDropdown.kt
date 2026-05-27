package com.learning.agentic_coding.ui.screens.kudos.compose.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosRecipient
import com.learning.agentic_coding.ui.screens.kudos.components.KudosAvatar

/**
 * Recipient (Người nhận) input + searchable dropdown. Matches MoMorph PV7jBVZU1N (field)
 * and 5MU728Tjck (open dropdown with avatar rows). Dropdown opens whenever the field is
 * focused and the query has no exact-name match.
 */
@Composable
fun RecipientDropdown(
    query: String,
    results: List<KudosRecipient>,
    selected: KudosRecipient?,
    onQueryChange: (String) -> Unit,
    onSelect: (KudosRecipient) -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    val dropdownVisible = focused && selected == null && results.isNotEmpty()

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = colorResource(R.color.saa_kudos_card_text),
                        fontSize = 13.sp,
                    ),
                    cursorBrush = SolidColor(colorResource(R.color.saa_kudos_card_text)),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChangedHack { focused = it },
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.kudo_compose_recipient_hint),
                                color = colorResource(R.color.saa_kudos_card_subtext),
                                fontSize = 13.sp,
                            )
                        }
                        inner()
                    },
                )
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_down),
                    contentDescription = null,
                    tint = colorResource(R.color.saa_kudos_card_text),
                    modifier = Modifier.size(16.dp).clickable {
                        // Re-open dropdown by clearing selection — keeps query intact.
                        if (selected != null) onSelect(selected.copy(name = selected.name))
                        focused = true
                    },
                )
            }
        }
        if (dropdownVisible) {
            Column(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.saa_kudos_pill_dark))
                    .heightIn(max = 220.dp),
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(results, key = { it.id }) { row ->
                        RecipientRow(row = row, onClick = {
                            onSelect(row)
                            focused = false
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipientRow(row: KudosRecipient, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        KudosAvatar(name = row.name, size = 36.dp, url = row.avatarUrl)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = row.name,
                color = colorResource(R.color.saa_text_primary),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = row.dept,
                color = colorResource(R.color.saa_text_secondary),
                fontSize = 11.sp,
            )
        }
    }
}

private fun Modifier.onFocusChangedHack(onChange: (Boolean) -> Unit): Modifier =
    this.onFocusChanged { onChange(it.isFocused) }
