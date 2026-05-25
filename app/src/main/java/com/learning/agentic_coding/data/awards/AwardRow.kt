package com.learning.agentic_coding.data.awards

import com.learning.agentic_coding.domain.Award
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wire-level shape for the `awards` table. Kept separate from [Award] so the domain
 * model stays free of serialization annotations and DB-specific fields.
 */
@Serializable
internal data class AwardRow(
    val id: String,
    val slug: String,
    val name: String,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("image_drawable") val imageDrawable: String,
    @SerialName("display_order") val displayOrder: Int,
    @SerialName("long_description") val longDescription: String = "",
    val quantity: Int = 0,
    @SerialName("quantity_unit") val quantityUnit: String = "",
    @SerialName("prize_value") val prizeValue: String = "",
    @SerialName("prize_note") val prizeNote: String = "",
    @SerialName("prize_value_secondary") val prizeValueSecondary: String? = null,
    @SerialName("prize_note_secondary") val prizeNoteSecondary: String? = null,
    @SerialName("long_description_en") val longDescriptionEn: String = "",
    @SerialName("long_description_ja") val longDescriptionJa: String = "",
    @SerialName("quantity_unit_en") val quantityUnitEn: String = "",
    @SerialName("quantity_unit_ja") val quantityUnitJa: String = "",
    @SerialName("prize_note_en") val prizeNoteEn: String = "",
    @SerialName("prize_note_ja") val prizeNoteJa: String = "",
    @SerialName("prize_note_secondary_en") val prizeNoteSecondaryEn: String? = null,
    @SerialName("prize_note_secondary_ja") val prizeNoteSecondaryJa: String? = null,
    @SerialName("name_en") val nameEn: String = "",
    @SerialName("name_ja") val nameJa: String = "",
) {
    fun toDomain(): Award = Award(
        id = id,
        slug = slug,
        name = name,
        shortDescription = shortDescription,
        imageDrawable = imageDrawable,
        displayOrder = displayOrder,
        longDescription = longDescription,
        quantity = quantity,
        quantityUnit = quantityUnit,
        prizeValue = prizeValue,
        prizeNote = prizeNote,
        prizeValueSecondary = prizeValueSecondary,
        prizeNoteSecondary = prizeNoteSecondary,
        longDescriptionEn = longDescriptionEn,
        longDescriptionJa = longDescriptionJa,
        quantityUnitEn = quantityUnitEn,
        quantityUnitJa = quantityUnitJa,
        prizeNoteEn = prizeNoteEn,
        prizeNoteJa = prizeNoteJa,
        prizeNoteSecondaryEn = prizeNoteSecondaryEn,
        prizeNoteSecondaryJa = prizeNoteSecondaryJa,
        nameEn = nameEn,
        nameJa = nameJa,
    )
}
