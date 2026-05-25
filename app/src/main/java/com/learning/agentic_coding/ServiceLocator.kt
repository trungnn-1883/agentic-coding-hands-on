package com.learning.agentic_coding

import android.content.Context
import com.learning.agentic_coding.data.auth.AuthRepository
import com.learning.agentic_coding.data.auth.GoogleSignInHelper
import com.learning.agentic_coding.data.auth.SupabaseProvider
import com.learning.agentic_coding.data.awards.AwardsRepository
import com.learning.agentic_coding.data.kudos.KudosRepository
import com.learning.agentic_coding.data.locale.LocaleRepository

/**
 * Lightweight service locator wired in [SaaApplication]. Avoids pulling in Hilt for an app of
 * this size; a future migration to Hilt would replace this file without touching the ViewModels.
 */
class ServiceLocator(applicationContext: Context) {
    val localeRepository: LocaleRepository = LocaleRepository(applicationContext)
    val authRepository: AuthRepository = AuthRepository(
        supabase = SupabaseProvider.client,
        googleSignInHelper = GoogleSignInHelper(),
    )
    val awardsRepository: AwardsRepository = AwardsRepository(SupabaseProvider.client)
    val kudosRepository: KudosRepository = KudosRepository(SupabaseProvider.client)
}

internal val Context.serviceLocator: ServiceLocator
    get() = (applicationContext as SaaApplication).services
