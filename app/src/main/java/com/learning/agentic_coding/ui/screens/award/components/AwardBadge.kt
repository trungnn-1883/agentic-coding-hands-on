package com.learning.agentic_coding.ui.screens.award.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Renders the award badge image when one exists in `drawable/` (e.g. `award_top_talent`,
 * `award_top_project`). For awards without a designed badge (Best Manager / Top Project
 * Leader / Signature / MVP per Figma) this composable renders nothing — those screens
 * go directly from the highlight block to the title row, matching the design.
 */
@Composable
fun AwardBadge(drawableName: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resId = remember(drawableName) {
        if (drawableName.isBlank()) 0
        else context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }
    if (resId == 0) return
    Box(
        modifier = modifier.size(160.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            painter = painterResource(resId),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
            contentScale = ContentScale.Fit,
        )
    }
}
