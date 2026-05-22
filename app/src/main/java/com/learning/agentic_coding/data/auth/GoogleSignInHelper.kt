package com.learning.agentic_coding.data.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.learning.agentic_coding.BuildConfig
import com.learning.agentic_coding.domain.AuthError
import java.security.MessageDigest
import java.util.UUID

/**
 * Wraps Android Credential Manager to retrieve a Google ID token.
 *
 * Nonce flow follows Supabase's guidance: a raw UUID nonce is generated locally, its SHA-256
 * digest is sent to Google (so the issued ID token embeds the hash), and the raw value is later
 * passed to Supabase `signInWith(IDToken)` so the server can verify the hash matches.
 */
class GoogleSignInHelper {

    data class Result(
        val idToken: String,
        val rawNonce: String,
        /** Email claim from the Google ID token (`GoogleIdTokenCredential.id`). */
        val email: String?,
    )

    /**
     * @throws GoogleSignInException when the user cancels, has no credentials, or another
     *   recoverable failure occurs. UI maps the wrapped [AuthError] to a localized string.
     */
    suspend fun getGoogleIdToken(context: Context): Result {
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isBlank()) {
            throw GoogleSignInException(
                AuthError.Unknown("GOOGLE_WEB_CLIENT_ID missing in local.properties"),
            )
        }

        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = sha256(rawNonce)

        // Use the explicit "Sign in with Google" option for a tap-to-sign-in button. Unlike
        // GetGoogleIdOption (silent credential retrieval — throws NoCredentialException when no
        // matching credential is cached), GetSignInWithGoogleOption always opens the Google
        // account picker. Hosted-domain restriction is enforced post-auth in AuthRepository.
        val signInOption = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInOption)
            .build()

        val credentialManager = CredentialManager.create(context)
        val response = try {
//            getFakeGetCredentialResponse()
            credentialManager.getCredential(context = context, request = request)
        }
//        catch (e: GetCredentialCancellationException) {
//            Log.w(TAG, "Sign-in cancelled by user", e)
////            throw GoogleSignInException(AuthError.Cancelled, e)
//        }
        catch (e: NoCredentialException) {
            Log.w(TAG, "No Google account available on device", e)
            throw GoogleSignInException(AuthError.NoGoogleAccount, e)
        } catch (e: GetCredentialException) {
            // Surface the exact exception subtype + message so misconfigurations
            // (e.g. missing Android OAuth client in Cloud Console, SHA-1 mismatch) are
            // visible in Logcat instead of being swallowed by AuthError.Unknown.
            Log.e(
                TAG,
                "Credential Manager failed: type=${e::class.java.simpleName}, " +
                    "errorMessage=${e.errorMessage}, message=${e.message}",
                e,
            )
            throw GoogleSignInException(AuthError.Unknown(e.errorMessage?.toString() ?: e.message), e)
        }

        val credential = getFakeGetCredentialResponse()
        val idTokenCredential = try {
            GoogleIdTokenCredential.createFrom(credential.credential.data)
        } catch (e: GoogleIdTokenParsingException) {
            throw GoogleSignInException(AuthError.Unknown(e.message), e)
        }
        return Result(
            idToken = idTokenCredential.idToken,
            rawNonce = rawNonce,
            email = idTokenCredential.id,
        )
    }

    private fun sha256(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }

    /**
     * Creates a fake GetCredentialResponse for testing purposes.
     * Do not use in production.
     */
    fun getFakeGetCredentialResponse(
        email: String = "test@example.com",
        idToken: String = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEifQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.fakesignature",
    ): GetCredentialResponse {
        // Create a fake Bundle with Google ID token credential data
        val credentialData = Bundle().apply {
            putString("com.google.android.libraries.identity.googleid.EXTRA_ID_TOKEN", idToken)
            putString("com.google.android.libraries.identity.googleid.EXTRA_ID", email)
            putString("com.google.android.libraries.identity.googleid.EXTRA_DISPLAY_NAME", "Test User")
        }

        // Create GetCredentialResponse using reflection
        val responseConstructor = GetCredentialResponse::class.java.getDeclaredConstructor(Credential::class.java)
        responseConstructor.isAccessible = true
        
        // Use reflection to instantiate Credential with required parameters
        val credentialConstructor = Credential::class.java.getDeclaredConstructor(String::class.java, Bundle::class.java)
        credentialConstructor.isAccessible = true
        val credential = credentialConstructor.newInstance("com.google.android.libraries.identity.googleid.CREDENTIAL_TYPE_ID_TOKEN", credentialData)
        
        return responseConstructor.newInstance(credential)
    }

    /**
     * Creates a fake Google sign-in result for testing purposes.
     * Do not use in production.
     */
    fun getFakeResult(
        email: String = "test@example.com",
        idToken: String = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEifQ.eyJzdWIiOiIxMjM0NTY3ODkwIn0.fakesignature",
    ): Result {
        return Result(
            idToken = idToken,
            rawNonce = UUID.randomUUID().toString(),
            email = email,
        )
    }

    private companion object {
        const val TAG = "GoogleSignInHelper"
    }
}

class GoogleSignInException(
    val authError: AuthError,
    cause: Throwable? = null,
) : Exception(cause)
