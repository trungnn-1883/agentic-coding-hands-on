package com.learning.agentic_coding.data.kudos

import com.learning.agentic_coding.domain.KudosHashtag
import com.learning.agentic_coding.domain.KudosRecipient
import com.learning.agentic_coding.domain.SecretBoxOpenResult
import com.learning.agentic_coding.domain.SecretBoxReward
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import java.util.UUID
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Reads the kudos_posts / kudos_user_stats / kudos_gift_recipients tables and writes new
 * kudos via [createKudo]. Lookup helpers feed the Send-Kudos dropdowns. Mirrors
 * AwardsRepository shape so UI layer can collect uniformly.
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
                    val rows = client.from("kudos_posts")
                        .select()
                        .decodeList<KudosPostRow>()
                        .sortedBy { it.displayOrder }
                    val images = client.from("kudo_images")
                        .select()
                        .decodeList<KudoImageRow>()
                        .sortedBy { it.sortOrder }
                        .groupBy { it.kudoId }
                    rows.map { row -> row.toDomain(images[row.id]?.map { it.imageUrl }.orEmpty()) }
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

    fun observeKudoDetail(id: String): Flow<KudoDetailResult> = flow {
        emit(KudoDetailResult.Loading)
        val client = supabase
        if (client == null) {
            emit(KudoDetailResult.Error("Supabase client not configured"))
            return@flow
        }
        val result = runCatching {
            coroutineScope {
                val postDeferred = async {
                    client.from("kudos_posts")
                        .select { filter { eq("id", id) } }
                        .decodeList<KudosPostRow>()
                        .firstOrNull()
                }
                val imagesDeferred = async {
                    client.from("kudo_images")
                        .select { filter { eq("kudo_id", id) } }
                        .decodeList<KudoImageRow>()
                        .sortedBy { it.sortOrder }
                        .map { it.imageUrl }
                }
                postDeferred.await() to imagesDeferred.await()
            }
        }
        emit(
            result.fold(
                onSuccess = { (row, images) ->
                    if (row == null) KudoDetailResult.Error("Kudo not found: $id")
                    else KudoDetailResult.Success(row.toDomain(images))
                },
                onFailure = { KudoDetailResult.Error(it.message ?: "Unknown error") },
            ),
        )
    }

    suspend fun listRecipients(): List<KudosRecipient> {
        val client = supabase ?: return emptyList()
        return runCatching {
            client.from("kudos_recipients")
                .select()
                .decodeList<KudosRecipientRow>()
                .sortedBy { it.displayOrder }
                .map { it.toDomain() }
        }.getOrDefault(emptyList())
    }

    suspend fun findRecipientByEmail(email: String): KudosRecipient? {
        val client = supabase ?: return null
        return runCatching {
            client.from("kudos_recipients")
                .select { filter { eq("email", email) } }
                .decodeList<KudosRecipientRow>()
                .firstOrNull()
                ?.toDomain()
        }.getOrNull()
    }

    /**
     * Realtime Sunner search (MoMorph hldqjHoSRH). Case-insensitive match on name OR email
     * against kudos_recipients, capped at [limit] rows. Blank query returns empty.
     */
    suspend fun searchRecipients(query: String, limit: Int = 10): List<KudosRecipient> {
        val client = supabase ?: return emptyList()
        val trimmed = query.trim()
        if (trimmed.isBlank()) return emptyList()
        // Escape ilike wildcards so a user typing % or _ doesn't broaden the match.
        val escaped = trimmed.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_")
        return runCatching {
            client.from("kudos_recipients")
                .select {
                    filter {
                        or {
                            ilike("name", "%$escaped%")
                            ilike("email", "%$escaped%")
                        }
                    }
                    limit(limit.toLong())
                }
                .decodeList<KudosRecipientRow>()
                .map { it.toDomain() }
        }.getOrDefault(emptyList())
    }

    suspend fun listHashtags(): List<KudosHashtag> {
        val client = supabase ?: return emptyList()
        return runCatching {
            client.from("kudos_hashtags")
                .select()
                .decodeList<KudosHashtagRow>()
                .sortedBy { it.displayOrder }
                .map { it.toDomain() }
        }.getOrDefault(emptyList())
    }

    /**
     * Inserts a new kudo and any attached images. Returns the new kudo id on success.
     * Image bytes are uploaded to the `kudo-images` storage bucket first; their public
     * URLs are written to the `kudo_images` table.
     */
    suspend fun createKudo(
        payload: KudosPostInsert,
        imageBytes: List<ByteArray>,
    ): Result<String> {
        val client = supabase ?: return Result.failure(IllegalStateException("Supabase not configured"))
        return runCatching {
            val inserted = client.from("kudos_posts")
                .insert(payload) { select(Columns.list("id")) }
                .decodeSingle<KudosInsertedRow>()
            val newId = inserted.id

            if (imageBytes.isNotEmpty()) {
                val bucket = client.storage.from("kudo-images")
                val imageRows = imageBytes.mapIndexed { idx, bytes ->
                    val path = "$newId/${UUID.randomUUID()}.jpg"
                    bucket.upload(path, bytes) { upsert = false }
                    val publicUrl = bucket.publicUrl(path)
                    KudoImageInsert(kudoId = newId, imageUrl = publicUrl, sortOrder = idx)
                }
                client.from("kudo_images").insert(imageRows)
            }
            newId
        }
    }

    /** Secret Box catalog (ordered for stable random / debug). */
    suspend fun listSecretBoxRewards(): List<SecretBoxReward> {
        val client = supabase ?: return emptyList()
        return runCatching {
            client.from("secret_box_rewards")
                .select()
                .decodeList<SecretBoxRewardRow>()
                .sortedBy { it.displayOrder }
                .map { it.toDomain() }
        }.getOrDefault(emptyList())
    }

    /**
     * Opens one Secret Box: picks a random reward, records the open in `secret_box_opens`,
     * then decrements `secret_box_unopened` (+ increments `secret_box_opened`) on the
     * single demo stats row. Returns the chosen reward + remaining count.
     *
     * Returns `reward = null` when the unopened pool is already empty.
     */
    suspend fun openSecretBox(): Result<SecretBoxOpenResult> {
        val client = supabase ?: return Result.failure(IllegalStateException("Supabase not configured"))
        return runCatching {
            val statsRow = client.from("kudos_user_stats")
                .select()
                .decodeList<KudosUserStatsRow>()
                .firstOrNull() ?: error("Stats row missing")
            val statsId = statsRow.id ?: error("Stats id missing")

            if (statsRow.secretBoxUnopened <= 0) {
                return@runCatching SecretBoxOpenResult(reward = null, unopenedRemaining = 0)
            }

            val rewards = listSecretBoxRewards()
            if (rewards.isEmpty()) error("Reward catalog empty")
            val picked = rewards.random()

            client.from("secret_box_opens")
                .insert(SecretBoxOpenInsert(rewardId = picked.id))

            val nextUnopened = (statsRow.secretBoxUnopened - 1).coerceAtLeast(0)
            val nextOpened = statsRow.secretBoxOpened + 1
            client.from("kudos_user_stats").update(
                SecretBoxStatsPatch(
                    secretBoxOpened = nextOpened,
                    secretBoxUnopened = nextUnopened,
                ),
            ) { filter { eq("id", statsId) } }

            SecretBoxOpenResult(reward = picked, unopenedRemaining = nextUnopened)
        }
    }
}
