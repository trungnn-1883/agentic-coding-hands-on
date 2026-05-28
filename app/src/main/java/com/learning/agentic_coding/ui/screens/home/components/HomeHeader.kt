package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.components.LanguageDropdown

/**
 * Home screen header — spec mms_1: SAA logo + language switcher + search + bell with badge.
 * Search/bell are no-op per clarifications (destination screens not yet built).
 */
@Composable
fun HomeHeader(
    language: Language,
    notificationUnread: Int,
    onLanguageSelect: (Language) -> Unit,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier,
    onInfoClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = painterResource(R.drawable.img_saa_logo),
            contentDescription = stringResource(R.string.home_logo_cd),
            modifier = Modifier.size(width = 48.dp, height = 44.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            LanguageDropdown(selected = language, onSelect = onLanguageSelect)
            if (onInfoClick != null) {
                IconButton(onClick = onInfoClick, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = stringResource(R.string.kudos_home_rules_cd),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            IconButton(onClick = onSearchClick, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.home_search_cd),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
            NotificationBell(unread = notificationUnread, onClick = onNotificationsClick)
        }
    }
}

@Composable
private fun NotificationBell(unread: Int, onClick: () -> Unit) {
    Box {
        IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = stringResource(R.string.home_notifications_cd),
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
        if (unread > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-10).dp, y = 10.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.saa_badge_red)),
            )
        }
    }
}
