package com.learning.agentic_coding.ui.screens.award.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

@Composable
fun KudosWordmark() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.ic_kudos_flame),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(width = 56.dp, height = 44.dp)
                .padding(end = 8.dp),
        )
        Text(
            text = stringResource(R.string.kudos_logo_text),
            color = colorResource(R.color.saa_kudos_logo),
            fontSize = 48.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-1.5).sp,
        )
    }
}