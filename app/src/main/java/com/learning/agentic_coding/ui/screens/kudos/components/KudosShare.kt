package com.learning.agentic_coding.ui.screens.kudos.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.learning.agentic_coding.R

/**
 * Writes the stub kudo deep-link to the system clipboard and shows a confirmation toast.
 * Shared by KudosCard (home, highlight, all-kudos) and KudoDetailScreen so behaviour
 * stays consistent. Pass [kudoId] so we can switch to per-kudo URIs once they exist.
 */
internal fun copyKudoLinkToClipboard(context: Context, @Suppress("UNUSED_PARAMETER") kudoId: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val link = context.getString(R.string.kudo_detail_share_link_stub)
    clipboard.setPrimaryClip(ClipData.newPlainText("Kudo link", link))
    Toast.makeText(
        context,
        context.getString(R.string.kudo_detail_copy_link_toast),
        Toast.LENGTH_SHORT,
    ).show()
}
