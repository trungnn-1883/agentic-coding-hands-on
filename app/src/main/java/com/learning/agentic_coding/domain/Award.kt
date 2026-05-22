package com.learning.agentic_coding.domain

/**
 * One award category card surfaced on the Home screen Awards section
 * (spec mms_4.2 — Top Talent / Top Project / etc).
 *
 * [imageDrawable] is the resource name (e.g. `award_top_talent`) so the UI layer
 * can resolve it without the repository knowing about Android resources.
 */
data class Award(
    val id: String,
    val slug: String,
    val name: String,
    val shortDescription: String,
    val imageDrawable: String,
    val displayOrder: Int,
)
