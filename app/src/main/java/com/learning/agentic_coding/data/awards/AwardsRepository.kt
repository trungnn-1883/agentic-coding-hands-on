package com.learning.agentic_coding.data.awards

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Reads the `awards` table from Supabase. Emits Loading first, then exactly one terminal
 * state: Success / Empty / Error. Caller drives retries by re-collecting the flow.
 */
class AwardsRepository(private val supabase: SupabaseClient) {

    fun observe(): Flow<AwardsResult> = flow {
        emit(AwardsResult.Loading)
        val result = runCatching {
            supabase.from("awards")
                .select()
                .decodeList<AwardRow>()
                .sortedBy { it.displayOrder }
                .map { it.toDomain() }
        }
        emit(
            result.fold(
                onSuccess = { awards ->
                    if (awards.isEmpty()) AwardsResult.Empty
                    else AwardsResult.Success(awards)
                },
                onFailure = { error ->
                    AwardsResult.Error(error.message ?: "Unknown error")
                },
            )
        )
    }
}
