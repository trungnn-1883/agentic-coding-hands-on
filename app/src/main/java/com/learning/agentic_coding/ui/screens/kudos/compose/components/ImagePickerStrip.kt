package com.learning.agentic_coding.ui.screens.kudos.compose.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.learning.agentic_coding.R

/**
 * Horizontal thumbnail strip + "+ Image (Tối đa N)" button. Uses the Android Photo Picker
 * (ActivityResultContracts.PickMultipleVisualMedia) so no runtime permission is needed.
 * Each thumbnail has a small red × in the top-right for removal.
 */
@Composable
fun ImagePickerStrip(
    uris: List<String>,
    maxCount: Int,
    onPick: (List<Uri>) -> Unit,
    onRemove: (String) -> Unit,
) {
    // PickMultipleVisualMedia requires maxItems >= 2; the VM caps the merged list to maxCount,
    // so a floor of 2 is safe even when only one slot remains.
    val remaining = (maxCount - uris.size).coerceAtLeast(2)
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(remaining),
        onResult = { picked -> if (picked.isNotEmpty()) onPick(picked) },
    )

    // Column so picked thumbnails stack ABOVE the add button (the caller drops this into a
    // Box slot; without the Column the two siblings would overlap).
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (uris.isNotEmpty()) {
            // Equal-weight row so all up-to-5 thumbnails fit the available width without
            // horizontal scrolling. Empty slots keep the picked images left-aligned and sized
            // consistently regardless of count.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                uris.forEach { uri ->
                    Thumbnail(
                        uri = uri,
                        onRemove = { onRemove(uri) },
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(maxCount - uris.size) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
        if (uris.size < maxCount) {
            AddImageButton(
                maxCount = maxCount,
                onClick = {
                    picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            )
        }
    }
}

@Composable
private fun Thumbnail(uri: String, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.aspectRatio(1f)) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp))
                .background(colorResource(R.color.saa_kudos_card_inner_bg)),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(16.dp)
                .clip(CircleShape)
                .background(colorResource(R.color.saa_kudos_hashtag_red))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "×", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AddImageButton(maxCount: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(1.dp, colorResource(R.color.saa_kudos_card_divider), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "+ Image",
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "(Tối đa $maxCount)",
            color = colorResource(R.color.saa_kudos_card_subtext),
            fontSize = 10.sp,
        )
    }
}
