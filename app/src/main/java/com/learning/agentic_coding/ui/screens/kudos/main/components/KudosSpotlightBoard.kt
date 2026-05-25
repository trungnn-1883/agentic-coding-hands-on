package com.learning.agentic_coding.ui.screens.kudos.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Spotlight board panel. Native render (see clarifications.md) — dark gradient background,
 * "388 KUDOS" gold title, sample names cloud rendered as small dim white text.
 */
@Composable
fun KudosSpotlightBoard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(124.dp)
            .padding(horizontal = 20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(124.dp)
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
                .padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.kudos_spotlight_count),
                    color = colorResource(R.color.saa_button_yellow),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                )
                SpotlightNamesCloud()
            }
        }
    }
}

@Composable
private fun SpotlightNamesCloud() {
    val rows = listOf(
        listOf("Huỳnh Dương Xuân", "Nguyễn Minh Châu", "Lê Hoàng Phúc", "Phạm Bình An"),
        listOf("Trần Mai", "Vũ Anh Tuấn", "Đỗ Thuý Hằng", "Phan Đạt", "Bùi Quang Huy"),
        listOf("Hoàng Thu Hà", "Trịnh Hà My", "Đặng Khánh Linh", "Mai Thanh Tùng"),
    )
    rows.forEach { row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            row.forEach { name ->
                Text(
                    text = name,
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
