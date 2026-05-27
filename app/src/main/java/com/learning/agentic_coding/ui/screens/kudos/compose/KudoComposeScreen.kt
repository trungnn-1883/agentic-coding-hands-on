package com.learning.agentic_coding.ui.screens.kudos.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.agentic_coding.R
import com.learning.agentic_coding.ui.screens.home.components.HomeBottomNav
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.kudos.components.KudosKvBackground
import com.learning.agentic_coding.ui.screens.kudos.compose.components.HashtagPicker
import com.learning.agentic_coding.ui.screens.kudos.compose.components.ImagePickerStrip
import com.learning.agentic_coding.ui.screens.kudos.compose.components.RecipientDropdown
import com.learning.agentic_coding.ui.screens.kudos.compose.components.FormatSpan
import com.learning.agentic_coding.ui.screens.kudos.compose.components.RichTextToolbar
import com.learning.agentic_coding.ui.screens.kudos.compose.components.SpanType
import com.learning.agentic_coding.ui.screens.kudos.compose.components.TextFormat
import com.learning.agentic_coding.ui.screens.kudos.compose.components.activeSpanTypes
import com.learning.agentic_coding.ui.screens.kudos.compose.components.applyPendingFormats
import com.learning.agentic_coding.ui.screens.kudos.compose.components.applyStructure
import com.learning.agentic_coding.ui.screens.kudos.compose.components.shiftSpans
import com.learning.agentic_coding.ui.screens.kudos.compose.components.styledTransformation
import com.learning.agentic_coding.ui.screens.kudos.compose.components.togglePending
import com.learning.agentic_coding.ui.screens.kudos.compose.components.toggleSpan

/**
 * Send-Kudos form (MoMorph PV7jBVZU1N + variants). Single scrolling card on the KV
 * background. Renders: top bar → form (recipient / award title / body w/ toolbar shell /
 * hashtag picker / image strip / anonymous toggle / nickname) → bottom Cancel + Send.
 */
@Composable
fun KudoComposeScreen(
    state: KudoComposeUiState,
    onBack: () -> Unit,
    onRecipientQuery: (String) -> Unit,
    onRecipientSelect: (com.learning.agentic_coding.domain.KudosRecipient) -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onBodySpansChange: (List<FormatSpan>) -> Unit,
    onPendingFormatsChange: (Set<SpanType>) -> Unit,
    onHashtagToggle: (com.learning.agentic_coding.domain.KudosHashtag) -> Unit,
    onHashtagRemove: (com.learning.agentic_coding.domain.KudosHashtag) -> Unit,
    onImagesPicked: (List<android.net.Uri>) -> Unit,
    onImageRemove: (String) -> Unit,
    onAnonymousToggle: () -> Unit,
    onAnonNicknameChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    var hashtagExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.saa_bg_dark)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f).statusBarsPadding()) {
                KudosKvBackground(heightDp = 900)
                Column(modifier = Modifier.fillMaxSize()) {
                    TopBar(onBack = onBack)
                    FormCard(
                        state = state,
                        hashtagExpanded = hashtagExpanded,
                        onRecipientQuery = onRecipientQuery,
                        onRecipientSelect = onRecipientSelect,
                        onTitleChange = onTitleChange,
                        onBodyChange = onBodyChange,
                        onBodySpansChange = onBodySpansChange,
                        onPendingFormatsChange = onPendingFormatsChange,
                        onHashtagToggle = onHashtagToggle,
                        onHashtagRemove = onHashtagRemove,
                        onHashtagExpandToggle = { hashtagExpanded = !hashtagExpanded },
                        onImagesPicked = onImagesPicked,
                        onImageRemove = onImageRemove,
                        onAnonymousToggle = onAnonymousToggle,
                        onAnonNicknameChange = onAnonNicknameChange,
                        onSubmit = onSubmit,
                        onCancel = onBack,
                    )
                }
            }
            HomeBottomNav(activeTab = HomeTab.KUDOS, onTabClick = onTabClick)
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(40.dp).clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.kudos_all_back_cd),
                tint = colorResource(R.color.saa_text_primary),
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = stringResource(R.string.kudo_compose_title),
            color = colorResource(R.color.saa_text_primary),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f).padding(end = 40.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun FormCard(
    state: KudoComposeUiState,
    hashtagExpanded: Boolean,
    onRecipientQuery: (String) -> Unit,
    onRecipientSelect: (com.learning.agentic_coding.domain.KudosRecipient) -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onBodySpansChange: (List<FormatSpan>) -> Unit,
    onPendingFormatsChange: (Set<SpanType>) -> Unit,
    onHashtagToggle: (com.learning.agentic_coding.domain.KudosHashtag) -> Unit,
    onHashtagRemove: (com.learning.agentic_coding.domain.KudosHashtag) -> Unit,
    onHashtagExpandToggle: () -> Unit,
    onImagesPicked: (List<android.net.Uri>) -> Unit,
    onImageRemove: (String) -> Unit,
    onAnonymousToggle: () -> Unit,
    onAnonNicknameChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            // Shrink the scroll viewport by the keyboard height so the focused field (notably
            // the bottom-most anonymous nickname) scrolls above the IME instead of being hidden
            // behind it — required because the app is edge-to-edge (window does not auto-resize).
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(colorResource(R.color.saa_kudos_card_bg))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = stringResource(R.string.kudo_compose_card_title),
                color = colorResource(R.color.saa_kudos_card_text),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            FieldRow(label = stringResource(R.string.kudo_compose_field_recipient), required = true) {
                RecipientDropdown(
                    query = state.recipientQuery,
                    results = state.filteredRecipients,
                    selected = state.selectedRecipient,
                    onQueryChange = onRecipientQuery,
                    onSelect = onRecipientSelect,
                )
            }

            FieldRow(label = stringResource(R.string.kudo_compose_field_award_title), required = true) {
                BoxedTextInput(
                    value = state.title,
                    placeholder = stringResource(R.string.kudo_compose_field_award_title_hint),
                    onChange = onTitleChange,
                )
            }
            Text(
                text = stringResource(R.string.kudo_compose_award_hint),
                color = colorResource(R.color.saa_kudos_card_subtext),
                fontSize = 11.sp,
            )

            BodyEditor(
                value = state.body,
                spans = state.bodySpans,
                pendingFormats = state.pendingFormats,
                onChange = onBodyChange,
                onSpansChange = onBodySpansChange,
                onPendingChange = onPendingFormatsChange,
            )
            Text(
                text = stringResource(R.string.kudo_compose_body_hint),
                color = colorResource(R.color.saa_kudos_card_subtext),
                fontSize = 11.sp,
            )

            FieldRow(label = stringResource(R.string.kudo_compose_field_hashtag), required = true) {
                HashtagPicker(
                    available = state.hashtags,
                    selected = state.selectedHashtags,
                    maxCount = state.maxHashtags,
                    expanded = hashtagExpanded,
                    onToggleExpanded = onHashtagExpandToggle,
                    onToggleSelect = onHashtagToggle,
                    onRemove = onHashtagRemove,
                )
            }

            FieldRow(label = stringResource(R.string.kudo_compose_field_image), required = false) {
                ImagePickerStrip(
                    uris = state.imageUris,
                    maxCount = state.maxImages,
                    onPick = onImagesPicked,
                    onRemove = onImageRemove,
                )
            }

            AnonymousToggleRow(
                isChecked = state.isAnonymous,
                onToggle = onAnonymousToggle,
            )

            if (state.isAnonymous) {
                FieldRow(label = stringResource(R.string.kudo_compose_field_nickname), required = true) {
                    BoxedTextInput(
                        value = state.anonNickname,
                        placeholder = "Doraemon",
                        onChange = onAnonNicknameChange,
                    )
                }
            }

            if (state.isSelfRecipient) {
                Text(
                    text = stringResource(R.string.kudo_compose_error_self),
                    color = colorResource(R.color.saa_kudos_hashtag_red),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (state.showError) {
                Text(
                    text = stringResource(R.string.kudo_compose_error_required),
                    color = colorResource(R.color.saa_kudos_hashtag_red),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            (state.submitState as? SubmitState.Error)?.let {
                Text(
                    text = it.message,
                    color = colorResource(R.color.saa_kudos_hashtag_red),
                    fontSize = 12.sp,
                )
            }
        }

        ActionButtonsRow(
            isSubmitting = state.submitState is SubmitState.Submitting,
            onCancel = onCancel,
            onSubmit = onSubmit,
        )
    }
}

@Composable
private fun FieldRow(label: String, required: Boolean, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.padding(top = 8.dp).fillMaxWidth(0.30f)) {
            Row {
                Text(
                    text = label,
                    color = colorResource(R.color.saa_kudos_card_text),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                if (required) {
                    Text(
                        text = " *",
                        color = colorResource(R.color.saa_kudos_hashtag_red),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) { content() }
    }
}

@Composable
private fun BoxedTextInput(value: String, placeholder: String, onChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            textStyle = TextStyle(
                color = colorResource(R.color.saa_kudos_card_text),
                fontSize = 13.sp,
            ),
            cursorBrush = SolidColor(colorResource(R.color.saa_kudos_card_text)),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = colorResource(R.color.saa_kudos_card_subtext),
                        fontSize = 13.sp,
                    )
                }
                inner()
            },
        )
    }
}

/**
 * Editor box (MoMorph spec C/D): a single bordered container holding the formatting toolbar,
 * a divider, then the message text area. Bold/Italic/Strikethrough are rendered live via
 * [SpanStyle] (kept in [spans], applied through a VisualTransformation); list/link/quote
 * insert plain structure. Plain text is pushed to the ViewModel via [onChange].
 */
@Composable
private fun BodyEditor(
    value: String,
    spans: List<FormatSpan>,
    pendingFormats: Set<SpanType>,
    onChange: (String) -> Unit,
    onSpansChange: (List<FormatSpan>) -> Unit,
    onPendingChange: (Set<SpanType>) -> Unit,
) {
    // Only the caret/selection is local — text + spans + pending live in the VM so they
    // survive navigating away and back (the composable's remembered state would be lost).
    var field by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }
    if (field.text != value) {
        // VM is source of truth for text content (e.g. truncation / reset after send).
        field = field.copy(text = value, selection = TextRange(value.length))
    }
    val onFieldChange: (TextFieldValue) -> Unit = { next ->
        if (next.text != field.text) {
            val shifted = shiftSpans(spans, field.text, next.text)
            onSpansChange(applyPendingFormats(shifted, field.text, next.text, pendingFormats))
            onChange(next.text)
        }
        field = next
    }
    val onFormat: (TextFormat) -> Unit = { format ->
        val spanType = format.spanType
        if (spanType != null) {
            // With a selection, style it now; with a bare caret, arm the sticky toggle.
            if (field.selection.collapsed) onPendingChange(togglePending(pendingFormats, spanType))
            else onSpansChange(toggleSpan(spans, field.selection, spanType))
        } else {
            onFieldChange(applyStructure(field, format))
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Toolbar above (Figma spec C), text area below (spec D). B/I/S light up when the
        // current selection already carries that style.

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(1.dp, colorResource(R.color.saa_kudos_card_divider), RoundedCornerShape(8.dp))
                .padding(10.dp),
        ) {
            BasicTextField(
                value = field,
                onValueChange = onFieldChange,
                textStyle = TextStyle(
                    color = colorResource(R.color.saa_kudos_card_text),
                    fontSize = 13.sp,
                ),
                visualTransformation = styledTransformation(spans),
                cursorBrush = SolidColor(colorResource(R.color.saa_kudos_card_text)),
                modifier = Modifier.fillMaxSize(),
                decorationBox = { inner ->
                    if (field.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.kudo_compose_body_placeholder),
                            color = colorResource(R.color.saa_kudos_card_subtext),
                            fontSize = 13.sp,
                        )
                    }
                    inner()
                },
            )
        }
        RichTextToolbar(
            onFormat = onFormat,
            // With a selection, light up only the formats the selected range actually carries
            // (empty for an unformatted region). Sticky pending toggles apply to the typing
            // caret only, so they count only when the selection is collapsed.
            activeFormats = activeSpanTypes(spans, field.selection).let {
                if (field.selection.collapsed) it + pendingFormats else it
            },
        )
    }
}

@Composable
private fun AnonymousToggleRow(isChecked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isChecked) colorResource(R.color.saa_button_yellow) else Color.White)
                .border(1.dp, colorResource(R.color.saa_kudos_card_text), RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (isChecked) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_small),
                    contentDescription = null,
                    tint = colorResource(R.color.saa_kudos_card_text),
                    modifier = Modifier.size(12.dp),
                )
            }
        }
        Text(
            text = stringResource(R.string.kudo_compose_anonymous),
            color = colorResource(R.color.saa_kudos_card_text),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun ActionButtonsRow(isSubmitting: Boolean, onCancel: () -> Unit, onSubmit: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ActionButton(
            label = stringResource(R.string.kudo_compose_cancel),
            background = Color.Black,
            textColor = Color.White,
            iconRes = R.drawable.ic_arrow_back,
            iconText = "×",
            modifier = Modifier.weight(1f),
            onClick = onCancel,
        )
        ActionButton(
            label = stringResource(R.string.kudo_compose_send),
            background = colorResource(R.color.saa_button_yellow),
            textColor = colorResource(R.color.saa_text_on_button),
            iconRes = R.drawable.ic_send_arrow,
            iconText = "▸",
            modifier = Modifier.weight(1f),
            enabled = !isSubmitting,
            onClick = onSubmit,
        )
    }
}

@Composable
private fun ActionButton(
    label: String,
    background: Color,
    textColor: Color,
    iconRes: Int,
    iconText: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val rowMod = if (enabled) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(background.copy(alpha = if (enabled) 1f else 0.5f))
            .then(rowMod)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier.size(18.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = iconText, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
