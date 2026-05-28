package com.learning.agentic_coding.domain

/** Catalog entry for a Secret Box reward (see Figma frames IXpGakYRm5, FvTOS7oCPU …). */
data class SecretBoxReward(
    val id: String,
    val name: String,
    /** Android drawable resource name (looked up via `resources.getIdentifier`). */
    val imageRes: String,
    val displayOrder: Int,
)

/** Outcome of an open attempt; null reward means the unopened pool is empty. */
data class SecretBoxOpenResult(
    val reward: SecretBoxReward?,
    val unopenedRemaining: Int,
)
