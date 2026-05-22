package com.learning.agentic_coding.ui.screens.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Theme description block — spec mms_3. Multi-line paragraph describing the
 * meaning of "Root Further" for SAA 2025. Display-only.
 */
@Composable
fun HomeThemeDescription(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.home_theme_description),
        color = colorResource(R.color.saa_text_primary),
        fontSize = 14.sp,
        lineHeight = 22.sp,
        modifier = modifier.padding(horizontal = 20.dp),
    )
}
