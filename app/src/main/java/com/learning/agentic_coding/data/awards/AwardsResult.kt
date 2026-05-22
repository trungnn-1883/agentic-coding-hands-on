package com.learning.agentic_coding.data.awards

import com.learning.agentic_coding.domain.Award

/**
 * Discrete states the Awards section can render — maps 1:1 to test cases
 * TC_IOS_HOME_GUI_002 (Loading), _003 (Empty), _004 (Error), and TC_FUN_003 (Retry).
 */
sealed interface AwardsResult {
    data object Loading : AwardsResult
    data class Success(val awards: List<Award>) : AwardsResult
    data object Empty : AwardsResult
    data class Error(val message: String) : AwardsResult
}
