package com.learning.agentic_coding.data.auth

import com.learning.agentic_coding.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

/**
 * App-wide Supabase client. URL + anon key are surfaced through [BuildConfig] from
 * `local.properties` (see `docs/auth-setup.md`).
 */
object SupabaseProvider {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
        ) {
            install(Auth)
        }
    }
}
