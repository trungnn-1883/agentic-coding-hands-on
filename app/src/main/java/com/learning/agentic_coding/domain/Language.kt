package com.learning.agentic_coding.domain

import androidx.annotation.DrawableRes
import com.learning.agentic_coding.R
import java.util.Locale

/**
 * Supported display languages for SAA 2025.
 *
 * Login spec (mms_2.1_language) is authoritative — supports VN / EN / JA per clarifications.
 * VN is the default per design (login spec defaultValue = "VN", TC_LOGIN_GUI_002).
 */
enum class Language(
    val code: String,
    val localeTag: String,
    @param:DrawableRes val flagRes: Int,
) {
    VN(code = "VN", localeTag = "vi", flagRes = R.drawable.ic_flag_vn),
    EN(code = "EN", localeTag = "en", flagRes = R.drawable.ic_flag_en),
    JA(code = "JA", localeTag = "ja", flagRes = R.drawable.ic_flag_ja);

    fun toLocale(): Locale = Locale.forLanguageTag(localeTag)

    companion object {
        val DEFAULT: Language = VN

        fun fromCode(code: String?): Language? =
            code?.let { value -> entries.firstOrNull { it.code.equals(value, ignoreCase = true) } }
    }
}
