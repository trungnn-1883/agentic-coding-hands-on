package com.learning.agentic_coding.ui.screens.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoTopBar

/**
 * Mock entry point — opened via Profile tab — to preview the Not Found / Access Denied
 * error screens until real 404/403 wiring lands. Intentionally minimal; not in the design system.
 */
@Composable
fun DebugMenuScreen(
    onBack: () -> Unit,
    onOpenNotFound: () -> Unit,
    onOpenAccessDenied: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            InfoTopBar(title = stringResource(R.string.debug_menu_title), onBack = onBack)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DebugButton(
                    labelRes = R.string.debug_menu_open_not_found,
                    onClick = onOpenNotFound,
                )
                DebugButton(
                    labelRes = R.string.debug_menu_open_access_denied,
                    onClick = onOpenAccessDenied,
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun DebugButton(labelRes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.saa_button_yellow),
            contentColor = colorResource(R.color.saa_text_on_button),
        ),
    ) {
        Text(
            text = stringResource(labelRes),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
