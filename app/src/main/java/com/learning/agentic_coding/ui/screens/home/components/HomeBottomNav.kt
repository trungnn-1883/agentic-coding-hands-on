package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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

/**
 * Bottom navigation bar — spec mms_7. Four tabs (SAA 2025 / Awards / Kudos / Profile).
 * Per clarifications, taps on non-active tabs are no-op; visual feedback only happens
 * for the SAA tab (always active on this screen).
 */
enum class HomeTab(val labelRes: Int, val iconRes: Int) {
    SAA(R.string.home_nav_saa, R.drawable.ic_nav_home),
    AWARDS(R.string.home_nav_awards, R.drawable.ic_nav_awards),
    KUDOS(R.string.home_nav_kudos, R.drawable.ic_nav_kudos),
    PROFILE(R.string.home_nav_profile, R.drawable.ic_nav_profile),
}

@Composable
fun HomeBottomNav(
    activeTab: HomeTab,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.saa_bg_dark))
            .navigationBarsPadding()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HomeTab.entries.forEach { tab ->
            NavBarTab(
                tab = tab,
                isActive = tab == activeTab,
                onClick = { onTabClick(tab) },
            )
        }
    }
}

@Composable
private fun NavBarTab(tab: HomeTab, isActive: Boolean, onClick: () -> Unit) {
    val tint = if (isActive) {
        colorResource(R.color.saa_button_yellow)
    } else {
        colorResource(R.color.saa_nav_inactive)
    }
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(tab.iconRes),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = stringResource(tab.labelRes),
            color = tint,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
        )
        if (isActive) {
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .size(width = 24.dp, height = 2.dp)
                    .background(colorResource(R.color.saa_button_yellow)),
            )
        }
    }
}
