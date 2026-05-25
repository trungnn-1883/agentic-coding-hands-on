package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.KudosGiftRecipient
import com.learning.agentic_coding.ui.screens.kudos.components.KudosAvatar

/** "10 SUNNER NHẬN QUÀ MỚI NHẤT" panel. Dark translucent card with avatar + two-line rows. */
@Composable
fun KudosGiftPanel(recipients: List<KudosGiftRecipient>, modifier: Modifier = Modifier) {
    if (recipients.isEmpty()) return
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.saa_kudos_panel_bg))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.kudos_gift_panel_title),
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        recipients.forEach { recipient ->
            GiftRow(recipient = recipient)
        }
    }
}

@Composable
private fun GiftRow(recipient: KudosGiftRecipient) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        KudosAvatar(name = recipient.name, size = 32.dp)
        Column {
            Text(
                text = recipient.name,
                color = colorResource(R.color.saa_button_yellow),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = recipient.giftText,
                color = colorResource(R.color.saa_text_primary),
                fontSize = 12.sp,
            )
        }
    }
}
