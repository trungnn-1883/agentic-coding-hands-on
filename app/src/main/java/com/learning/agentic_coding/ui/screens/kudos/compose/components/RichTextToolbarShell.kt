package com.learning.agentic_coding.ui.screens.kudos.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R

/**
 * Formatting actions exposed by the toolbar (MoMorph spec C). [spanType] is non-null for the
 * three character styles applied via [SpanStyle]; the rest insert plain markdown structure.
 */
enum class TextFormat(val spanType: SpanType?) {
    Bold(SpanType.Bold),
    Italic(SpanType.Italic),
    Strikethrough(SpanType.Strikethrough),
    NumberedList(null),
    Link(null),
    Quote(null),
}

/** Character-level styles rendered live in the editor via [SpanStyle]. */
enum class SpanType {
    Bold, Italic, Strikethrough;

    val style: SpanStyle
        get() = when (this) {
            Bold -> SpanStyle(fontWeight = FontWeight.Bold)
            Italic -> SpanStyle(fontStyle = FontStyle.Italic)
            Strikethrough -> SpanStyle(textDecoration = TextDecoration.LineThrough)
        }
}

/** A styled range over the body text. Stored separately from the plain text content. */
data class FormatSpan(val start: Int, val end: Int, val type: SpanType)

/**
 * Toolbar row (B / I / S / list / link / quote) + the "Tiêu chuẩn cộng đồng" link, inside a
 * rounded bordered box matching MoMorph spec C. B/I/S are styled letters; the rest are icons.
 */
@Composable
fun RichTextToolbar(
    onFormat: (TextFormat) -> Unit,
    activeFormats: Set<SpanType>,
    modifier: Modifier = Modifier,
    onOpenCommunityStandards: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, colorResource(R.color.saa_kudos_card_divider), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        GlyphButton(
            "B",
            TextStyle(fontWeight = FontWeight.Bold),
            SpanType.Bold in activeFormats
        ) { onFormat(TextFormat.Bold) }
        GlyphButton(
            "I",
            TextStyle(fontStyle = FontStyle.Italic),
            SpanType.Italic in activeFormats
        ) { onFormat(TextFormat.Italic) }
        GlyphButton(
            "S",
            TextStyle(textDecoration = TextDecoration.LineThrough),
            SpanType.Strikethrough in activeFormats
        ) { onFormat(TextFormat.Strikethrough) }
        IconButton(R.drawable.ic_format_list_numbered) { onFormat(TextFormat.NumberedList) }
        IconButton(R.drawable.ic_format_link) { onFormat(TextFormat.Link) }
        IconButton(R.drawable.ic_format_quote) { onFormat(TextFormat.Quote) }
        Box(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(18.dp)
                .background(colorResource(R.color.saa_kudos_card_divider)),
        )
        Text(
            text = "Tiêu chuẩn cộng đồng",
            color = colorResource(R.color.saa_kudos_hashtag_red),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,

            modifier = Modifier
                .padding(start = 10.dp)
                .clickable(onClick = onOpenCommunityStandards),
        )
    }
}

@Composable
private fun GlyphButton(
    text: String,
    style: TextStyle = TextStyle.Default,
    active: Boolean = false,
    onClick: () -> Unit,
) {
    // Active = the current selection already carries this style (spec C: "Toggles active when
    // enabled"). Render a filled accent chip with inverted text so the state reads at a glance.
    val bg = if (active) colorResource(R.color.saa_button_yellow) else Color.Transparent
    val fg = colorResource(R.color.saa_kudos_card_text)
    Box(
        modifier = Modifier
            .size(width = 22.dp, height = 24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = fg, fontSize = 14.sp, style = style)
    }
}

@Composable
private fun IconButton(iconRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colorResource(R.color.saa_kudos_card_text),
            modifier = Modifier.size(16.dp),
        )
    }
}

/**
 * Builds a [VisualTransformation] that renders [spans] over the editor text using [SpanStyle].
 * Text length is unchanged, so offset mapping is identity.
 */
fun styledTransformation(spans: List<FormatSpan>): VisualTransformation =
    VisualTransformation { source ->
        val styled = buildAnnotatedString {
            append(source.text)
            spans.forEach { span ->
                val s = span.start.coerceIn(0, source.length)
                val e = span.end.coerceIn(s, source.length)
                if (e > s) addStyle(span.type.style, s, e)
            }
        }
        TransformedText(styled, OffsetMapping.Identity)
    }

/**
 * Toggles a [type] span over [selection]: removes any same-type span overlapping the range
 * (toggle-off), otherwise adds it. No-op when the selection is empty.
 */
fun toggleSpan(spans: List<FormatSpan>, selection: TextRange, type: SpanType): List<FormatSpan> {
    val start = selection.min
    val end = selection.max
    if (end <= start) return spans
    val overlapping = spans.filter { it.type == type && it.start < end && it.end > start }
    return if (overlapping.isNotEmpty()) spans - overlapping.toSet()
    else spans + FormatSpan(start, end, type)
}

/**
 * Toggles a sticky [type] in the [pending] set. Pending formats stay enabled (the toolbar
 * button stays lit) until toggled off again, and are applied to subsequently typed text —
 * spec C: "Toggles active when … is enabled".
 */
fun togglePending(pending: Set<SpanType>, type: SpanType): Set<SpanType> =
    if (type in pending) pending - type else pending + type

/**
 * Applies each sticky [pending] format to the text added by an edit. The inserted range is
 * [common-prefix length, +delta); only additive edits (delta > 0) gain new spans, so
 * deletions and caret moves leave existing spans untouched. No-op when nothing is pending.
 */
fun applyPendingFormats(
    spans: List<FormatSpan>,
    oldText: String,
    newText: String,
    pending: Set<SpanType>,
): List<FormatSpan> {
    if (pending.isEmpty()) return spans
    val delta = newText.length - oldText.length
    if (delta <= 0) return spans
    val changeAt = oldText.commonPrefixWith(newText).length
    return spans + pending.map { FormatSpan(changeAt, changeAt + delta, it) }
}

/**
 * The set of styles active for [selection] — used to light up the toolbar buttons. A
 * non-empty selection is active when a same-type span overlaps it; a collapsed cursor is
 * active when it sits strictly inside a span.
 */
fun activeSpanTypes(spans: List<FormatSpan>, selection: TextRange): Set<SpanType> {
    val start = selection.min
    val end = selection.max
    return spans.filter { span ->
        if (end > start) span.start < end && span.end > start
        else span.start < start && span.end > start
    }.map { it.type }.toSet()
}

/**
 * Remaps spans after a text edit. Finds the common prefix as the change point and shifts span
 * boundaries at/after it by the length delta, dropping any that collapse to empty.
 */
fun shiftSpans(spans: List<FormatSpan>, oldText: String, newText: String): List<FormatSpan> {
    if (oldText == newText) return spans
    val changeAt = oldText.commonPrefixWith(newText).length
    val delta = newText.length - oldText.length
    return spans.mapNotNull { span ->
        val s = if (span.start >= changeAt) span.start + delta else span.start
        val e = if (span.end >= changeAt) span.end + delta else span.end
        val cs = s.coerceIn(0, newText.length)
        val ce = e.coerceIn(0, newText.length)
        if (ce > cs) span.copy(start = cs, end = ce) else null
    }
}

/** Inserts plain markdown structure for list / link / quote (non-span formats). */
fun applyStructure(value: TextFieldValue, format: TextFormat): TextFieldValue {
    val text = value.text
    val start = value.selection.min
    val end = value.selection.max
    return when (format) {
        TextFormat.Link -> {
            val selected = text.substring(start, end).ifEmpty { "text" }
            val insert = "[$selected](url)"
            val newText = text.substring(0, start) + insert + text.substring(end)
            val urlStart = start + insert.indexOf("url")
            TextFieldValue(newText, TextRange(urlStart, urlStart + 3))
        }

        TextFormat.NumberedList, TextFormat.Quote -> {
            val prefix = if (format == TextFormat.NumberedList) "1. " else "> "
            val lineStart = text.lastIndexOf('\n', (start - 1).coerceAtLeast(0))
                .let { if (it < 0) 0 else it + 1 }
            val newText = text.substring(0, lineStart) + prefix + text.substring(lineStart)
            TextFieldValue(newText, TextRange(start + prefix.length))
        }

        else -> value
    }
}
