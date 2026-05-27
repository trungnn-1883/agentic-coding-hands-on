package com.learning.agentic_coding.data.kudos

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.learning.agentic_coding.domain.KudosBadge
import com.learning.agentic_coding.domain.KudosRecipient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private val Context.recentSearchDataStore by preferencesDataStore(name = "kudos_recent_searches")

/**
 * Persists the last [MAX_RECENTS] Sunner search picks locally (MoMorph 3jgwke3E8O "Recent"
 * section). Stored as a JSON array of [RecentSunner]; survives app restarts. Most-recent
 * first; re-picking an existing entry moves it to the top (no duplicates).
 */
class RecentSearchStore(private val context: Context) {

    val recentsFlow: Flow<List<KudosRecipient>> = context.recentSearchDataStore.data
        .map { prefs ->
            val raw = prefs[KEY_RECENTS] ?: return@map emptyList()
            runCatching { json.decodeFromString<List<RecentSunner>>(raw) }
                .getOrDefault(emptyList())
                .map { it.toDomain() }
        }

    suspend fun add(recipient: KudosRecipient) {
        context.recentSearchDataStore.edit { prefs ->
            val current = prefs[KEY_RECENTS]
                ?.let { runCatching { json.decodeFromString<List<RecentSunner>>(it) }.getOrDefault(emptyList()) }
                .orEmpty()
            val entry = RecentSunner.from(recipient)
            val deduped = listOf(entry) + current.filterNot { it.id == entry.id }
            prefs[KEY_RECENTS] = json.encodeToString(LIST_SERIALIZER, deduped.take(MAX_RECENTS))
        }
    }

    suspend fun remove(id: String) {
        context.recentSearchDataStore.edit { prefs ->
            val current = prefs[KEY_RECENTS]
                ?.let { runCatching { json.decodeFromString<List<RecentSunner>>(it) }.getOrDefault(emptyList()) }
                .orEmpty()
            prefs[KEY_RECENTS] = json.encodeToString(LIST_SERIALIZER, current.filterNot { it.id == id })
        }
    }

    private companion object {
        const val MAX_RECENTS = 5
        val KEY_RECENTS = stringPreferencesKey("recent_sunners_json")
        val json = Json { ignoreUnknownKeys = true }
        val LIST_SERIALIZER = ListSerializer(RecentSunner.serializer())
    }
}

@Serializable
private data class RecentSunner(
    val id: String,
    val email: String? = null,
    val name: String,
    val dept: String,
    val badge: String,
    val avatarUrl: String? = null,
) {
    fun toDomain(): KudosRecipient =
        KudosRecipient(id, email, name, dept, KudosBadge.fromWire(badge), avatarUrl)

    companion object {
        fun from(r: KudosRecipient) =
            RecentSunner(r.id, r.email, r.name, r.dept, r.badge.wire, r.avatarUrl)
    }
}
