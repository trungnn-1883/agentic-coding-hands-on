package com.learning.agentic_coding.ui.screens.error

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.ui.theme.MyApplicationTheme

/**
 * Shared scaffold for both error screens (MoMorph sn2mdavs1a [iOS] Not Found and
 * k-7zJk2B7s [iOS] Access denied). Layout identical between the two — only the title,
 * description, and illustration vary.
 */
@Composable
fun ErrorScreen(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    @DrawableRes illustrationRes: Int,
    @StringRes illustrationDescriptionRes: Int,
    onBack: () -> Unit,
    onGoHome: () -> Unit,
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
            ErrorTopBar(onBack = onBack)
            ErrorHeader(titleRes = titleRes, descriptionRes = descriptionRes)
            Spacer(Modifier.height(24.dp))
            ErrorIllustration(
                illustrationRes = illustrationRes,
                contentDescriptionRes = illustrationDescriptionRes,
            )
            Spacer(Modifier.height(24.dp))
            ErrorDivider()
            Spacer(Modifier.height(24.dp))
            ErrorGoHomeButton(onClick = onGoHome)
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun ErrorTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.error_back_cd),
                tint = colorResource(R.color.saa_text_primary),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun ErrorHeader(@StringRes titleRes: Int, @StringRes descriptionRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(titleRes),
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
        )
        ErrorDivider()
        Text(
            text = stringResource(descriptionRes),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 320.dp),
        )
    }
}

@Composable
private fun ErrorIllustration(
    @DrawableRes illustrationRes: Int,
    @StringRes contentDescriptionRes: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(illustrationRes),
            contentDescription = stringResource(contentDescriptionRes),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(320f / 249f),
        )
    }
}

@Composable
private fun ErrorDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(R.color.saa_award_divider)),
    )
}

@Composable
private fun ErrorGoHomeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(40.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.saa_button_yellow),
            contentColor = colorResource(R.color.saa_text_on_button),
        ),
    ) {
        Text(
            text = stringResource(R.string.error_go_home),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun NotFoundScreen(onBack: () -> Unit, onGoHome: () -> Unit, modifier: Modifier = Modifier) {
    ErrorScreen(
        titleRes = R.string.error_404_title,
        descriptionRes = R.string.error_404_description,
        illustrationRes = R.drawable.img_error_404,
        illustrationDescriptionRes = R.string.error_404_illustration_cd,
        onBack = onBack,
        onGoHome = onGoHome,
        modifier = modifier,
    )
}

@Composable
fun AccessDeniedScreen(onBack: () -> Unit, onGoHome: () -> Unit, modifier: Modifier = Modifier) {
    ErrorScreen(
        titleRes = R.string.error_403_title,
        descriptionRes = R.string.error_403_description,
        illustrationRes = R.drawable.img_access_denied,
        illustrationDescriptionRes = R.string.error_403_illustration_cd,
        onBack = onBack,
        onGoHome = onGoHome,
        modifier = modifier,
    )
}

@Preview(name = "Not Found", showBackground = true, locale = "en")
@Composable
private fun NotFoundScreenPreview() {
    MyApplicationTheme {
        NotFoundScreen(onBack = {}, onGoHome = {})
    }
}

@Preview(name = "Access Denied", showBackground = true, locale = "en")
@Composable
private fun AccessDeniedScreenPreview() {
    MyApplicationTheme {
        AccessDeniedScreen(onBack = {}, onGoHome = {})
    }
}
