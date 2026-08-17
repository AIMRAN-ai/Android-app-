package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Query("SELECT * FROM notifications WHERE userId = :userId AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getByUser(userId: String): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE userId = :userId AND isRead = 0 AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getUnreadByUser(userId: String): Flow<List<NotificationEntity>>

    @Query("UPDATE notifications SET isRead = 1, readAt = :readAt WHERE id = :id")
    suspend fun markAsRead(id: String, readAt: Long = System.currentTimeMillis())

    @Query("UPDATE notifications SET isRead = 1, readAt = :readAt WHERE userId = :userId AND isRead = 0")
    suspend fun markAllAsRead(userId: String, readAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM notifications WHERE userId = :userId AND createdAt < :olderThan")
    suspend fun deleteOlderThan(userId: String, olderThan: Long)

    @Query("SELECT * FROM notifications WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<NotificationEntity>>
}