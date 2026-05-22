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
) {
    fun toDomain(): Award = Award(
        id = id,
        slug = slug,
        name = name,
        shortDescription = shortDescription,
        imageDrawable = imageDrawable,
        displayOrder = displayOrder,
    )
}
