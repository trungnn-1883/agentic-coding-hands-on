package com.learning.agentic_coding.data.locale

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.learning.agentic_coding.domain.Language

/**
 * Wraps [content] so that `stringResource` and `LocalConfiguration` resolve against the chosen
 * [language]. Activity does NOT recreate — Compose recomposes with the new locale immediately,
 * satisfying TC_LANGDD_FUN_007 / _008 ("không cần reload").
 */
@Composable
fun LocalizedContent(language: Language, content: @Composable () -> Unit) {
    val baseContext = LocalContext.current
    val configuration = remember(language) {
        Configuration(baseContext.resources.configuration).apply {
            val locale = language.toLocale()
            setLocale(locale)
            setLayoutDirection(locale)
        }
    }
    val localizedContext = remember(configuration) {
        baseContext.createConfigurationContext(configuration)
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides configuration,
    ) {
        content()
    }
}
