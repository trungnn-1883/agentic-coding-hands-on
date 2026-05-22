package com.learning.agentic_coding.data.auth

import android.content.Context
import android.util.Log
import com.learning.agentic_coding.domain.AuthError
import com.learning.agentic_coding.domain.AuthUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import java.io.IOException

/**
 * Orchestrates Google sign-in through Credential Manager and exchanges the resulting ID token for
 * a Supabase session.
 *
 * Session restoration (TC_LOGIN_ACC_002 / TC_LOGIN_FUN_012) is delegated to Supabase's default
 * storage; consumers observe [currentUser] to react to restored sessions.
 *
 * No domain restriction — any Google account with a valid ID token can sign in.
 */
class AuthRepository(
    private val supabase: SupabaseClient,
    private val googleSignInHelper: GoogleSignInHelper,
) {

    private val _mockUser = MutableStateFlow<AuthUser?>(null)

    val currentUser: Flow<AuthUser?> = merge(
        supabase.auth.sessionStatus.map { status ->
            (status as? SessionStatus.Authenticated)?.session?.user?.let { user ->
                AuthUser(
                    id = user.id,
                    email = user.email ?: "",
                    displayName = user.userMetadata?.get("name")?.toString()
                        ?: user.userMetadata?.get("full_name")?.toString(),
                )
            }
        },
        _mockUser,
    )

    suspend fun signInWithGoogle(context: Context) {
        val google = try {
            googleSignInHelper.getGoogleIdToken(context)
        }
        catch (e: GoogleSignInException) {
            throw AuthRepositoryException(e.authError, e)
        }

        try {
            supabase.auth.signInWith(IDToken) {
                idToken = google.idToken
                provider = Google
                nonce = google.rawNonce
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network failure during Supabase signInWith(IDToken)", e)
            throw AuthRepositoryException(AuthError.Network, e)
        } catch (e: Exception) {
            Log.e(TAG, "Supabase signInWith(IDToken) failed: ${e.message}", e)
            throw AuthRepositoryException(AuthError.Unknown(e.message), e)
        }
    }

    suspend fun signOut() {
        runCatching { supabase.auth.signOut() }
        _mockUser.value = null
    }

    /**
     * Mock sign-in for testing/development. Creates a fake authenticated session.
     * This allows testing the login flow without needing real Google credentials.
     */
    suspend fun mockSignIn() {
        // Create a mock user that will be emitted through currentUser Flow
        _mockUser.value = AuthUser(
            id = "mock-user-id-12345",
            email = "ngoctrung@gmail.com",
            displayName = "Nguyen Ngoc Trung C",
        )
        Log.i(TAG, "Mock sign-in successful - user will navigate to Home screen")
    }

    private companion object {
        const val TAG = "AuthRepository"
    }
}

class AuthRepositoryException(
    val authError: AuthError,
    cause: Throwable? = null,
) : Exception(cause)
