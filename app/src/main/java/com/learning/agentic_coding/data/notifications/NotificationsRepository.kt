package com.learning.agentic_coding.data.notifications

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

/**
 * Reads/updates the `notifications` table. [observeAll] feeds the screen list; mutation
 * helpers ([markAsRead], [markAllRead]) refresh both the list and the [unreadCount]
 * StateFlow that bell badges across the app consume.
 */
class NotificationsRepository(private val supabase: SupabaseClient? = null) {

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    fun observeAll(): Flow<NotificationsResult> = flow {
        emit(NotificationsResult.Loading)
        val client = supabase
        if (client == null) {
            emit(NotificationsResult.Error("Supabase client not configured"))
            return@flow
        }
        val result = runCatching {
            client.from("notifications")
                .select()
                .decodeList<NotificationRow>()
                .sortedBy { it.displayOrder }
                .map { it.toDomain() }
        }
        result.onSuccess { items -> _unreadCount.value = items.count { !it.isRead } }
        emit(
            result.fold(
                onSuccess = { NotificationsResult.Success(it) },
                onFailure = { NotificationsResult.Error(it.message ?: "Unknown error") },
            ),
        )
    }

    suspend fun refreshUnreadCount(): Int {
        val client = supabase ?: return 0
        val count = runCatching {
            client.from("notifications")
                .select()
                .decodeList<NotificationRow>()
                .count { !it.isRead }
        }.getOrDefault(0)
        _unreadCount.value = count
        return count
    }

    suspend fun markAsRead(id: String): Result<Unit> {
        val client = supabase ?: return Result.failure(IllegalStateException("Supabase not configured"))
        return runCatching {
            client.from("notifications")
                .update(NotificationReadPatch(isRead = true)) { filter { eq("id", id) } }
            refreshUnreadCount()
            Unit
        }
    }

    suspend fun markAllRead(): Result<Unit> {
        val client = supabase ?: return Result.failure(IllegalStateException("Supabase not configured"))
        return runCatching {
            client.from("notifications")
                .update(NotificationReadPatch(isRead = true)) { filter { eq("is_read", false) } }
            _unreadCount.value = 0
            Unit
        }
    }
}
