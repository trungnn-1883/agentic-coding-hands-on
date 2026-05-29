package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Spotlight board panel (Figma fO0Kt19sZZ mms_B.7_Spotlight). 159dp dark gradient
 * panel framed with a 0.29dp olive border. Layout:
 *   - top row: search-sunner pill (left/right) + "388 KUDOS" gold title (center).
 *   - middle: deterministic name-wall of ~80 tiny contributor names scattered
 *     across the board with random size and opacity to mimic a 3D depth field.
 *   - bottom: notification ticker line ("08:30PM ... đã nhận được một Kudos mới").
 *
 * Native render; the original Figma node is a static raster of a pan/zoom canvas.
 */
@Composable
fun KudosSpotlightBoard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(159.dp)
            .padding(horizontal = 20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            colorResource(R.color.saa_gradient_purple),
                            colorResource(R.color.saa_gradient_blue),
                            colorResource(R.color.saa_gradient_orange),
                        ),
                    ),
                )
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.saa_kudos_border_olive),
                    shape = RoundedCornerShape(8.dp),
                ),
        ) {
            SpotlightNamesWall()
            SpotlightHeader()
            SpotlightTicker()
        }
    }
}

@Composable
private fun SpotlightHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SearchSunnerPill()
        Text(
            text = stringResource(R.string.kudos_spotlight_count),
            color = colorResource(R.color.saa_button_yellow),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
        )
        // Symmetric spacer matching the search pill's footprint so "388 KUDOS"
        // stays optically centered.
        Box(modifier = Modifier.size(width = 80.dp, height = 1.dp))
    }
}

@Composable
private fun SearchSunnerPill() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colorResource(R.color.saa_kudos_yellow_tint_10))
            .border(
                width = 1.dp,
                color = colorResource(R.color.saa_kudos_border_olive),
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null,
            tint = colorResource(R.color.saa_text_primary),
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = stringResource(R.string.kudos_spotlight_search),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SpotlightTicker() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text = stringResource(R.string.kudos_spotlight_ticker),
            color = colorResource(R.color.saa_button_yellow).copy(alpha = 0.95f),
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

/**
 * Scatter ~80 contributor names across the board using a fixed seed so the layout
 * is stable across recompositions. Each name gets a randomized position, size,
 * and opacity to create a depth-of-field word-wall effect matching the Figma render.
 */
@Composable
private fun SpotlightNamesWall() {
    val names = remember { SpotlightNames }
    val placements = remember {
        val rng = Random(seed = SpotlightSeed)
        names.map { name ->
            val depth = rng.nextFloat()
            NamePlacement(
                name = name,
                xFraction = rng.nextFloat(),
                yFraction = 0.18f + rng.nextFloat() * 0.6f, // keep clear of header + ticker
                fontSize = (4 + depth * 5).toInt(), // 4..9 sp
                alpha = 0.25f + depth * 0.55f, // 0.25..0.80
            )
        }
    }
    Layout(
        content = {
            placements.forEach { p ->
                Text(
                    text = p.name,
                    color = Color.White.copy(alpha = p.alpha),
                    fontSize = p.fontSize.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { measurables, constraints ->
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(childConstraints) }
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val p = placements[index]
                val x = (p.xFraction * (constraints.maxWidth - placeable.width)).roundToInt()
                val y = (p.yFraction * (constraints.maxHeight - placeable.height)).roundToInt()
                placeable.place(IntOffset(x, y))
            }
        }
    }
}

private data class NamePlacement(
    val name: String,
    val xFraction: Float,
    val yFraction: Float,
    val fontSize: Int,
    val alpha: Float,
)

// Names from Figma mms_B.7_Spotlight node siblings — repeated to fill the wall density.
private val SpotlightNames: List<String> = buildList {
    val pool = listOf(
        "Đỗ Hoàng Hiệp", "Dương Thúy An", "Mai Phương Thúy", "Nguyễn Văn Quy",
        "Lê Kiều Trang", "Nguyễn Bá Chức", "Nguyễn Hoàng Linh", "Huỳnh Dương Xuân",
    )
    repeat(10) { addAll(pool) }
}

// Stable PRNG seed so the name wall is deterministic across recompositions.
private const val SpotlightSeed: Int = 0x4B55444F // "KUDO"
