package com.learning.agentic_coding.ui.screens.kudos.detail

import com.learning.agentic_coding.data.kudos.KudoDetailResult
import com.learning.agentic_coding.domain.Language

/** UI state for the Kudo detail screen. Heart toggle lives locally — no DB write yet. */
data class KudoDetailUiState(
    val language: Language = Language.DEFAULT,
    val data: KudoDetailResult = KudoDetailResult.Loading,
    val heartDelta: Int = 0,
    val isLiked: Boolean = false,
)
