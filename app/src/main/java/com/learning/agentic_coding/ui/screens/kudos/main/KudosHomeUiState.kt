package com.learning.agentic_coding.ui.screens.kudos.main

import com.learning.agentic_coding.data.kudos.KudosResult
import com.learning.agentic_coding.domain.Language

/**
 * Aggregate snapshot for the Kudos home screen. Filter selections are tracked in the
 * ViewModel and applied on the highlight carousel when [data] is Success.
 */
data class KudosHomeUiState(
    val language: Language = Language.DEFAULT,
    val data: KudosResult = KudosResult.Loading,
    val departmentFilter: String? = null,
    val hashtagFilter: String? = null,
    val notificationUnread: Int = 0,
)
