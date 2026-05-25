package com.learning.agentic_coding.domain

/**
 * One award category surfaced on Home + Award Detail screens
 * (spec mms_4.2 + Award Detail frames).
 *
 * VN fields ([longDescription], [quantityUnit], [prizeNote], [prizeNoteSecondary]) are
 * the default. The `*_en` / `*_ja` columns hold translations; consumers should use
 * [longDescriptionFor], [quantityUnitFor], [prizeNoteFor], [prizeNoteSecondaryFor] to
 * resolve the right locale at render time. Empty translations fall back to VN.
 *
 * [prizeValueSecondary] / [prizeNoteSecondary*] are populated only for awards with
 * two prize tiers (e.g. Signature 2025: individual + team).
 */
data class Award(
    val id: String,
    val slug: String,
    val name: String,
    val shortDescription: String,
    val imageDrawable: String,
    val displayOrder: Int,
    val longDescription: String = "",
    val quantity: Int = 0,
    val quantityUnit: String = "",
    val prizeValue: String = "",
    val prizeNote: String = "",
    val prizeValueSecondary: String? = null,
    val prizeNoteSecondary: String? = null,
    val longDescriptionEn: String = "",
    val longDescriptionJa: String = "",
    val quantityUnitEn: String = "",
    val quantityUnitJa: String = "",
    val prizeNoteEn: String = "",
    val prizeNoteJa: String = "",
    val prizeNoteSecondaryEn: String? = null,
    val prizeNoteSecondaryJa: String? = null,
    val nameEn: String = "",
    val nameJa: String = "",
) {
    fun nameFor(language: Language): String = when (language) {
        Language.VN -> name
        Language.EN -> nameEn.ifBlank { name }
        Language.JA -> nameJa.ifBlank { name }
    }

    fun longDescriptionFor(language: Language): String = when (language) {
        Language.VN -> longDescription
        Language.EN -> longDescriptionEn.ifBlank { longDescription }
        Language.JA -> longDescriptionJa.ifBlank { longDescription }
    }

    fun quantityUnitFor(language: Language): String = when (language) {
        Language.VN -> quantityUnit
        Language.EN -> quantityUnitEn.ifBlank { quantityUnit }
        Language.JA -> quantityUnitJa.ifBlank { quantityUnit }
    }

    fun prizeNoteFor(language: Language): String = when (language) {
        Language.VN -> prizeNote
        Language.EN -> prizeNoteEn.ifBlank { prizeNote }
        Language.JA -> prizeNoteJa.ifBlank { prizeNote }
    }

    fun prizeNoteSecondaryFor(language: Language): String? = when (language) {
        Language.VN -> prizeNoteSecondary
        Language.EN -> prizeNoteSecondaryEn?.ifBlank { prizeNoteSecondary } ?: prizeNoteSecondary
        Language.JA -> prizeNoteSecondaryJa?.ifBlank { prizeNoteSecondary } ?: prizeNoteSecondary
    }
}
