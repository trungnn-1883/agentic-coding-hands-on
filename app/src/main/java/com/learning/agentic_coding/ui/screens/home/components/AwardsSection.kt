package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.learning.agentic_coding.data.awards.AwardsResult
import com.learning.agentic_coding.domain.Award

/**
 * Awards section — spec mms_4. Renders the eyebrow + section title, then branches
 * on [state] to show Loading spinner / Empty label / Error+Retry / horizontally
 * scrolling [AwardCard] list (TC_GUI_002, _003, _004 + TC_FUN_003).
 */
@Composable
fun AwardsSection(
    state: AwardsResult,
    onAwardClick: (Award) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionEyebrow(
            eyebrow = stringResource(R.string.home_awards_event_label),
            title = stringResource(R.string.home_awards_section_title),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        when (state) {
            is AwardsResult.Loading -> AwardsLoading()
            is AwardsResult.Empty -> AwardsMessage(
                text = stringResource(R.string.home_awards_empty),
                showRetry = false,
                onRetry = {},
            )
            is AwardsResult.Error -> AwardsMessage(
                text = stringResource(R.string.home_awards_error),
                showRetry = true,
                onRetry = onRetry,
            )
            is AwardsResult.Success -> AwardsList(awards = state.awards, onAwardClick = onAwardClick)
        }
    }
}

@Composable
internal fun SectionEyebrow(eyebrow: String, title: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = eyebrow,
            color = colorResource(R.color.saa_text_dim),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = title,
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun AwardsLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = colorResource(R.color.saa_button_yellow),
            modifier = Modifier.size(36.dp),
        )
    }
}

@Composable
private fun AwardsMessage(text: String, showRetry: Boolean, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            color = colorResource(R.color.saa_text_secondary),
            fontSize = 14.sp,
        )
        if (showRetry) {
            Button(
                onClick = onRetry,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(36.dp)
                    .width(120.dp),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.saa_button_yellow),
                    contentColor = colorResource(R.color.saa_text_on_button),
                ),
            ) {
                Text(
                    text = stringResource(R.string.home_awards_retry),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun AwardsList(awards: List<Award>, onAwardClick: (Award) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = awards, key = { it.id }) { award ->
            AwardCard(award = award, onClick = { onAwardClick(award) })
        }
    }
}
