package com.learning.agentic_coding.ui.screens.kudos.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.learning.agentic_coding.domain.KudosRecipient
import com.learning.agentic_coding.ui.screens.kudos.components.KudosAvatar

/**
 * One Sunner row in the search/recents list (MoMorph 3jgwke3E8O item / hldqjHoSRH result).
 * Avatar + name + dept. When [onRemove] is non-null, a trailing × removes it from recents.
 */
@Composable
fun SunnerListItem(
    sunner: KudosRecipient,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        KudosAvatar(name = sunner.name, size = 44.dp, url = sunner.avatarUrl)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = sunner.name,
                color = colorResource(R.color.saa_text_primary),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = sunner.dept,
                color = colorResource(R.color.saa_text_secondary),
                fontSize = 12.sp,
            )
        }
        if (onRemove != null) {
            Box(
                modifier = Modifier.size(28.dp).clickable(onClick = onRemove),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.sunner_search_remove_cd),
                    tint = colorResource(R.color.saa_text_secondary),
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
