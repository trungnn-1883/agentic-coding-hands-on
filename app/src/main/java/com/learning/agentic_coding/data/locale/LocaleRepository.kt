package com.learning.agentic_coding.data.locale

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.learning.agentic_coding.domain.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.localeDataStore by preferencesDataStore(name = "saa_locale_prefs")

/**
 * Persists the user's selected display language across app launches.
 *
 * Default = [Language.DEFAULT] (VN). The flow emits the current selection on every change so the
 * UI can react instantly (TC_LANGDD_FUN_007 / _008: "không cần reload").
 */
class LocaleRepository(private val context: Context) {

    val languageFlow: Flow<Language> = context.localeDataStore.data
        .map { prefs -> Language.fromCode(prefs[KEY_LANGUAGE]) ?: Language.DEFAULT }

    suspend fun setLanguage(language: Language) {
        context.localeDataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language.code
        }
    }

    private companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language_code")
    }
}
