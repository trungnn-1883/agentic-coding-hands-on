# Authentication Setup — SAA 2025

This app uses Supabase Auth with Google sign-in driven by Android Credential Manager. The flow:

```
Credential Manager (Google ID token)
      │ rawNonce ──hash──> hashedNonce ─► Google
      │                                  │
      │            idToken + rawNonce ◄──┘
      ▼
supabase.auth.signInWith(IDToken) { idToken, provider = Google, nonce = rawNonce }
      │
      ▼
SessionStatus.Authenticated  ─►  AuthRepository emits AuthUser
                                    │
                                    └─ if email is not @sun-asterisk.com → signOut + InvalidAccount
```

## Local development checklist

1. **Run Supabase locally**

   ```
   cd /Users/nguyen.ngoc.trungc/AndroidStudioProjects/MyApplication
   supabase start
   ```

   Copy the `anon key` it prints into `local.properties` (see below). The default `SUPABASE_URL`
   already points at `http://10.0.2.2:54321` (the Android emulator alias for the host machine).

2. **Create a Google OAuth Web Client**

   - Open Google Cloud Console → **APIs & Services → Credentials**.
   - **Create credentials → OAuth client ID → Application type: Web application**.
   - Add an authorized redirect URI:
     `http://10.0.2.2:54321/auth/v1/callback` (Supabase local).
   - Copy the resulting client ID (ends with `.apps.googleusercontent.com`).

2a. **Create a matching Android OAuth client (CRITICAL)**

   This step is required even though you'll keep using the Web Client ID in code. Without it,
   the Credential Manager picker opens, the user picks an account, and Google's backend then
   returns `GetCredentialResponse error returned from framework` because it can't validate the
   calling app.

   - Same project as the Web client in Google Cloud Console → **Create credentials → OAuth
     client ID → Application type: Android**.
   - **Package name:** `com.learning.agentic_coding`
   - **SHA-1 certificate fingerprint:** run this in the project root to find your debug SHA-1:
     ```
     ./gradlew :app:signingReport
     ```
     Copy the `SHA1:` line under `Variant: debug`. Paste it into the Android client form.
   - Save. Propagation takes 1–5 minutes.
   - You do NOT need to copy the resulting Android client ID into the app — `GetSignInWithGoogleOption`
     only takes the Web Client ID; Google looks up the Android client by `(package + SHA-1)`.

   For release builds, repeat with the release keystore's SHA-1.

3. **Enable Google provider in Supabase**

   Edit `supabase/config.toml`:

   ```toml
   [auth.external.google]
   enabled = true
   client_id = "env(SUPABASE_AUTH_EXTERNAL_GOOGLE_CLIENT_ID)"
   secret   = "env(SUPABASE_AUTH_EXTERNAL_GOOGLE_SECRET)"
   skip_nonce_check = false
   ```

   Restart Supabase: `supabase stop && supabase start`.

4. **Wire secrets into the app**

   Edit `local.properties` (already gitignored):

   ```properties
   SUPABASE_URL=http://10.0.2.2:54321
   SUPABASE_ANON_KEY=<paste anon key from supabase start>
   GOOGLE_WEB_CLIENT_ID=<paste Google Web Client ID>
   ```

   Re-sync Gradle so BuildConfig picks them up.

5. **No domain restriction**

   Any Google account with a valid ID token can sign in. The earlier `@sun-asterisk.com`
   enforcement was removed on 2026-05-21 per session reversal; see `plans/260521-1320-login-and-language-dropdown/clarifications.md`.

## File map

| Concern | File |
|---|---|
| Supabase client | `data/auth/SupabaseProvider.kt` |
| Google credential picker | `data/auth/GoogleSignInHelper.kt` |
| Sign-in orchestration | `data/auth/AuthRepository.kt` |
| Session restoration | Supabase default `SettingsSessionManager` (encrypted shared prefs) |
| Language persistence | `data/locale/LocaleRepository.kt` (DataStore Preferences) |
| Runtime locale switch | `data/locale/LocalizedContent.kt` (Compose wrapper, no Activity recreate) |
| Login UI | `ui/screens/login/LoginScreen.kt` + `LoginViewModel.kt` |
| Home placeholder | `ui/screens/home/HomeScreen.kt` + `HomeViewModel.kt` |
| Language dropdown component | `ui/components/LanguageDropdown.kt` |

## Test cases mapped

| Test ID | Where implemented |
|---|---|
| TC_LOGIN_ACC_001 (Login screen shows when unauthenticated) | `SaaApp.kt` routes by `currentUser == null` |
| TC_LOGIN_ACC_002 (Authenticated user → Home) | `SaaApp.kt` routes by `currentUser != null` (Supabase restores session at startup) |
| TC_LOGIN_GUI_002 (Default = VN) | `Language.DEFAULT = VN`; `LocaleRepository` returns DEFAULT when DataStore empty |
| TC_LOGIN_FUN_001 (Allowed langs VN/EN/JA) | `Language` enum lists exactly these three |
| TC_LOGIN_FUN_003 (Selecting language updates flag + label) | `LanguageDropdown` re-renders on `selected` change |
| TC_LOGIN_FUN_004 (Language change re-renders all login text) | `LocalizedContent` wraps content with new locale; Compose recomposes |
| TC_LOGIN_FUN_005 (Tap Login starts Google OAuth) | `LoginViewModel.onLoginClick` → `AuthRepository.signInWithGoogle` |
| TC_LOGIN_FUN_006 (Loading state) | `state.isLoading` toggles button to `CircularProgressIndicator` |
| TC_LOGIN_FUN_007 (Navigate to Home on success) | `SaaApp` switches to `HomeRoute` when `currentUser != null` |
| TC_LOGIN_FUN_008 (Double-click prevention) | `if (localState.value.isLoading) return` in `LoginViewModel` |
| TC_LOGIN_FUN_010 (Auth failure shows error) | `AuthRepositoryException(AuthError.*)` → `state.error` → localized text |
| TC_LOGIN_FUN_012 (Auto-login with valid token) | Supabase `SettingsSessionManager` restores session at app start |
| TC_LOGIN_FUN_013 (Expired token → Login) | Supabase clears session on refresh failure; `currentUser` flow emits `null` |
| TC_LOGIN_FUN_014 (Login after logout) | `HomeViewModel.onLogout()` → `supabase.auth.signOut()` |
| TC_LOGIN_FUN_015 (Disabled / blocked Google account rejected) | Surfaces as `AuthError.Unknown` from Credential Manager / Supabase; domain restriction was removed per 2026-05-21 reversal |
| TC_LANGDD_ACC_001 (Dropdown visible on Login) | `TopBar` in `LoginScreen` |
| TC_LANGDD_FUN_001 / _002 (Open / close) | `expanded = !expanded` toggle in `LanguageDropdown` |
| TC_LANGDD_FUN_007 / _008 (Instant re-render) | `LocalizedContent` recomposes inline |

## Known gaps

- The Sun\* Annual Awards logo, background keyvisual, and Root Further wordmark are vector
  approximations — drop the official PNG assets into `app/src/main/res/drawable-*dpi/` to replace.
- No splash screen — initial `currentUser` is `null` until Supabase finishes session restore,
  so a brief login flicker can occur. Mitigate with a splash screen if it bothers QA.
