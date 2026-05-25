package com.learning.agentic_coding.ui.screens.kudos.all

import com.learning.agentic_coding.data.kudos.KudosResult
import com.learning.agentic_coding.domain.Language

/** State for the All-Kudos detail screen. Reuses [KudosResult] from the repository. */
data class AllKudosUiState(
    val language: Language = Language.DEFAULT,
    val data: KudosResult = KudosResult.Loading,
)
