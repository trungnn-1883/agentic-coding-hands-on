package com.learning.agentic_coding.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.AuthUser
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.components.LanguageDropdown

/**
 * Placeholder Home / dashboard. Created per clarifications so navigation tests
 * (TC_LOGIN_FUN_007 / _009 / _012) have a real destination — full dashboard out of scope.
 */
@Composable
fun HomeRoute(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        user = state.user,
        language = state.language,
        onLanguageSelect = viewModel::onLanguageSelect,
        onLogout = viewModel::onLogout,
    )
}

@Composable
fun HomeScreen(
    user: AuthUser?,
    language: Language,
    onLanguageSelect: (Language) -> Unit,
    onLogout: () -> Unit,
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
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                LanguageDropdown(selected = language, onSelect = onLanguageSelect)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    R.string.home_welcome,
                    user?.displayName ?: user?.email.orEmpty(),
                ),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.saa_button_yellow),
                    contentColor = colorResource(R.color.saa_text_on_button),
                ),
            ) {
                Text(
                    text = stringResource(R.string.home_logout),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
