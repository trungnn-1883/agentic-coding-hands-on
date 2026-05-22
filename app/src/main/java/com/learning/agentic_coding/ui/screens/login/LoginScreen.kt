package com.learning.agentic_coding.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.AuthError
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.components.LanguageDropdown

/**
 * Login screen — Figma node 8HGlvYGJWq ([iOS] Login).
 *
 * Layout follows the 375×812 reference (iPhone). Vertical positions are reproduced via fixed
 * Spacers between content blocks; the middle band uses `Modifier.weight` so the layout stretches
 * on taller phones without breaking element sizes.
 */
@Composable
fun LoginRoute(viewModel: LoginViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LoginScreen(
        state = state,
        onLanguageSelect = viewModel::onLanguageSelect,
        onLoginClick = { viewModel.onLoginClick(context) },
    )
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onLanguageSelect: (Language) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        // Layer 1: keyvisual artwork (Figma node 6885:8965) drawn full-bleed.
        Image(
            painter = painterResource(R.drawable.bg_keyvisual),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        // Layer 2: horizontal dim — full-bleed 90deg gradient sitting above the keyvisual and
        // below all content. Darkens the left side so the wordmark/description/button stay
        // readable while the artwork still bleeds through on the right.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(saaHorizontalDimGradient()),
        )
        // Layer 3: top header gradient overlay (Figma node 6885:8972, 375×104 @ opacity 0.9).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .alpha(0.9f)
                .background(saaHeaderGradient()),
        )

        // Layer 4: content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
        ) {
            // SAA logo (48×44 at design Y=52) + language dropdown (90×32 at design Y=64).
            // Status bar is 44dp in design; subtracting it: row starts 8dp below the inset.
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_saa_logo),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.size(width = 48.dp, height = 44.dp),
                )
                LanguageDropdown(
                    selected = state.language,
                    onSelect = onLanguageSelect,
                )
            }

            // Gap 96 → 252 = 156dp.
            Spacer(modifier = Modifier.height(156.dp))

            // ROOT FURTHER wordmark (Figma node 6885:8967 → exported asset), 247×109.
            Image(
                painter = painterResource(R.drawable.img_root_further),
                contentDescription = stringResource(R.string.login_root_further),
                modifier = Modifier.size(width = 247.dp, height = 109.dp),
            )

            // Gap 361 → 393 = 32dp.
            Spacer(modifier = Modifier.height(32.dp))

            // Description (Figma node 6885:8968): 335×40, Montserrat 14sp light, white.
            Text(
                text = stringResource(R.string.login_description),
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Light,
            )

            // Flexible band: design has 193dp here at iPhone 8; ratio 193:114 between gaps.
            Spacer(modifier = Modifier.weight(1.7f))

            // LOGIN button (Figma node 6885:8969): 246×40, borderRadius 4, #FFEA9E, centered.
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                GoogleLoginButton(
                    isLoading = state.isLoading,
                    onClick = onLoginClick,
                )
            }

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(state.error.toMessageRes()),
                    color = colorResource(R.color.saa_error),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Copyright (Figma node 6885:8971): 12sp Montserrat 400, white, centered.
            Text(
                text = stringResource(R.string.login_copyright),
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )
        }
    }
}

@Composable
private fun GoogleLoginButton(
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .width(246.dp)
            .height(40.dp),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.saa_button_yellow),
            contentColor = colorResource(R.color.saa_text_on_button),
            disabledContainerColor = colorResource(R.color.saa_button_yellow),
            disabledContentColor = colorResource(R.color.saa_text_on_button),
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = colorResource(R.color.saa_text_on_button),
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = stringResource(R.string.login_button_google),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

/** Header dim — base #00101A at varying alpha (Figma node 6885:8972). */
private fun saaHeaderGradient(): Brush = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF00101A),
        0.7644f to Color(0x4D00101A),
        0.8462f to Color(0x3300101A),
        0.8870f to Color(0x2600101A),
        0.9279f to Color(0x1A00101A),
        0.9639f to Color(0x0D00101A),
        1.0f to Color(0x0000101A),
    ),
)

/**
 * Horizontal dim — `linear-gradient(90deg, #00101A 0%, rgba(0,16,26,0.9) 28.97%,
 *   rgba(0,16,26,0.6) 54.82%, rgba(0,16,26,0) 93.61%)` from the Figma spec.
 *
 * Alpha hex: 100% = 0xFF, 90% = 0xE6, 60% = 0x99, 0% = 0x00.
 */
private fun saaHorizontalDimGradient(): Brush = Brush.horizontalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF00101A),
        0.2897f to Color(0xE600101A),
        0.5482f to Color(0x9900101A),
        0.9361f to Color(0x0000101A),
    ),
)

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun LoginScreenPreviewDefault() {
    LoginScreen(
        state = LoginUiState(),
        onLanguageSelect = {},
        onLoginClick = {},
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun LoginScreenPreviewLoading() {
    LoginScreen(
        state = LoginUiState(isLoading = true),
        onLanguageSelect = {},
        onLoginClick = {},
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun LoginScreenPreviewError() {
    LoginScreen(
        state = LoginUiState(error = AuthError.NoGoogleAccount),
        onLanguageSelect = {},
        onLoginClick = {},
    )
}
