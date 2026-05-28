package com.learning.agentic_coding.ui.screens.kudos.secretbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.domain.SecretBoxReward
import com.learning.agentic_coding.ui.screens.kudos.info.components.InfoTopBar

/**
 * Open Secret Box screen — maps to MoMorph [iOS] Open secret box (kQk65hSYF2) and its
 * Standby reveal variants. Three visible states:
 *   - Idle:     box image + "Click vào box để mở" + unopened counter
 *   - Opening:  brief spinner overlay while the repository call resolves
 *   - Revealed: reward image + "Chúc mừng…" headline + reward name (tap anywhere → Idle)
 */
@Composable
fun OpenBoxScreen(
    state: OpenBoxUiState,
    onBack: () -> Unit,
    onBoxTap: () -> Unit,
    onDismissReveal: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        SecretBoxBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            InfoTopBar(
                title = stringResource(R.string.secret_box_top_title),
                onBack = onBack,
            )
            OpenBoxBody(
                state = state,
                onBoxTap = onBoxTap,
                onDismissReveal = onDismissReveal,
                onRetry = onRetry,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

/**
 * Mostly-dark background matching the Figma [iOS] Open secret box frames. Keeps a soft
 * top-right silhouette from the keyvisual art so the screen stays branded but isn't washed
 * out by the colorful smoke seen on the Tiêu chuẩn / Thể lệ info pages.
 */
@Composable
private fun SecretBoxBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Image(
            painter = painterResource(R.drawable.saa_keyvisual_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.18f),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            alignment = Alignment.TopCenter,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorResource(R.color.saa_bg_dark).copy(alpha = 0.55f),
                            colorResource(R.color.saa_bg_dark).copy(alpha = 0.92f),
                            colorResource(R.color.saa_bg_dark),
                        ),
                    ),
                ),
        )
    }
}

@Composable
private fun OpenBoxBody(
    state: OpenBoxUiState,
    onBoxTap: () -> Unit,
    onDismissReveal: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        OpenBoxUiState.Loading -> CenteredSpinner(modifier)
        is OpenBoxUiState.Failure -> FailurePlaceholder(message = state.message, onRetry = onRetry, modifier = modifier)
        is OpenBoxUiState.Idle -> IdleContent(unopenedCount = state.unopenedCount, onBoxTap = onBoxTap, modifier = modifier)
        is OpenBoxUiState.Opening -> IdleContent(
            unopenedCount = state.unopenedCount,
            onBoxTap = {}, // disabled while opening
            overlay = { Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f))) { CenteredSpinner() } },
            modifier = modifier,
        )
        is OpenBoxUiState.Revealed -> RevealedContent(
            reward = state.reward,
            onDismiss = onDismissReveal,
            modifier = modifier,
        )
    }
}

@Composable
private fun IdleContent(
    unopenedCount: Int,
    onBoxTap: () -> Unit,
    overlay: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val isEmpty = unopenedCount <= 0
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            HeadlineTitle(text = stringResource(R.string.secret_box_title_explore))
            Spacer(modifier = Modifier.height(12.dp))
            Subtitle(
                text = if (isEmpty) {
                    stringResource(R.string.secret_box_subtitle_empty)
                } else {
                    stringResource(R.string.secret_box_subtitle_click)
                },
            )
            Spacer(modifier = Modifier.height(28.dp))
            BoxPodium(onTap = if (isEmpty) ({}) else onBoxTap, dimmed = isEmpty)
            Spacer(modifier = Modifier.height(28.dp))
            UnopenedCounter(count = unopenedCount)
        }
        overlay?.invoke()
    }
}

@Composable
private fun RevealedContent(
    reward: SecretBoxReward,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onDismiss)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(72.dp))
        HeadlineTitle(text = stringResource(R.string.secret_box_reveal_subtitle))
        Spacer(modifier = Modifier.height(28.dp))
        RewardPodium(reward = reward)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = reward.name,
            color = colorResource(R.color.saa_award_label_gold),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HeadlineTitle(text: String) {
    Text(
        text = text,
        color = colorResource(R.color.saa_award_label_gold),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Subtitle(text: String) {
    Text(
        text = text,
        color = colorResource(R.color.saa_text_primary),
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

/**
 * Box on gold podium — uses the cropped Figma asset directly (img_secret_box). Single
 * Image sized to the parent so it scales cleanly across devices.
 */
@Composable
private fun BoxPodium(onTap: () -> Unit, dimmed: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(345f / 365f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onTap),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.img_secret_box),
            contentDescription = stringResource(R.string.secret_box_box_cd),
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (dimmed) 0.45f else 1f),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
        )
    }
}

@Composable
private fun RewardPodium(reward: SecretBoxReward) {
    val context = LocalContext.current
    val resId = remember(reward.imageRes) {
        context.resources.getIdentifier(reward.imageRes, "drawable", context.packageName)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(345f / 365f)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(resId),
                contentDescription = stringResource(R.string.secret_box_reward_cd),
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            )
        } else {
            // Defensive: missing drawable just renders the name as a fallback.
            Text(
                text = reward.name,
                color = colorResource(R.color.saa_text_primary),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun UnopenedCounter(count: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        colorResource(R.color.saa_award_label_gold).copy(alpha = 0.4f),
                        Color.Transparent,
                    ),
                ),
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.secret_box_unopened_label),
                color = colorResource(R.color.saa_text_primary),
                fontSize = 14.sp,
            )
            Text(
                text = "%02d".format(count),
                color = colorResource(R.color.saa_text_primary),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun CenteredSpinner(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colorResource(R.color.saa_button_yellow))
    }
}

@Composable
private fun FailurePlaceholder(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = colorResource(R.color.saa_error),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.home_awards_retry),
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onRetry),
        )
    }
}
