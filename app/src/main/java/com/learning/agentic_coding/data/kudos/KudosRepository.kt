package com.learning.agentic_coding.data.kudos

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Reads the kudos_posts / kudos_user_stats / kudos_gift_recipients tables. Emits Loading
 * first, then exactly one terminal Success / Error state. Mirrors AwardsRepository shape so
 * UI layer can collect uniformly.
 *
 * [isKudosAvailable] retained as a static gate consumed by HomeViewModel (spec FUN_009).
 */
class KudosRepository(private val supabase: SupabaseClient? = null) {

    val isKudosAvailable: Boolean = true

    fun observeHome(): Flow<KudosResult> = flow {
        emit(KudosResult.Loading)
        val client = supabase
        if (client == null) {
            emit(KudosResult.Error("Supabase client not configured"))
            return@flow
        }
        val result = runCatching {
            coroutineScope {
                val postsDeferred = async {
                    client.from("kudos_posts")
                        .select()
                        .decodeList<KudosPostRow>()
                        .sortedBy { it.displayOrder }
                        .map { it.toDomain() }
                }
                val statsDeferred = async {
                    client.from("kudos_user_stats")
                        .select()
                        .decodeList<KudosUserStatsRow>()
                        .firstOrNull()
                        ?.toDomain()
                }
                val giftsDeferred = async {
                    client.from("kudos_gift_recipients")
                        .select()
                        .decodeList<KudosGiftRecipientRow>()
                        .sortedBy { it.displayOrder }
                        .map { it.toDomain() }
                }
                Triple(postsDeferred.await(), statsDeferred.await(), giftsDeferred.await())
            }
        }
        emit(
            result.fold(
                onSuccess = { (posts, stats, gifts) ->
                    if (stats == null) {
                        KudosResult.Error("Stats row missing")
                    } else {
                        KudosResult.Success(posts = posts, stats = stats, giftRecipients = gifts)
                    }
                },
                onFailure = { KudosResult.Error(it.message ?: "Unknown error") },
            ),
        )
    }
}
