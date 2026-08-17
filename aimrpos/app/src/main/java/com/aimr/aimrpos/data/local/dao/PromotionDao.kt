package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.PromotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(promotion: PromotionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(promotions: List<PromotionEntity>)

    @Query("SELECT * FROM promotions WHERE id = :id")
    suspend fun getById(id: String): PromotionEntity?

    @Query("SELECT * FROM promotions WHERE isDeleted = 0 AND isActive = 1 ORDER BY startDate DESC")
    fun getAllActive(): Flow<List<PromotionEntity>>

    @Query("SELECT * FROM promotions WHERE isDeleted = 0 ORDER BY startDate DESC")
    fun getAll(): Flow<List<PromotionEntity>>

    @Query("SELECT * FROM promotions WHERE isDeleted = 0 AND isActive = 1 AND startDate <= :now AND (endDate IS NULL OR endDate >= :now) ORDER BY startDate DESC")
    fun getCurrentlyActive(now: Long = System.currentTimeMillis()): Flow<List<PromotionEntity>>

    @Query("SELECT * FROM promotions WHERE isDeleted = 0 AND createdBy = :userId ORDER BY startDate DESC")
    fun getByUser(userId: String): Flow<List<PromotionEntity>>

    @Query("DELETE FROM promotions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE promotions SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsage(id: String)

    @Query("UPDATE promotions SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("SELECT * FROM promotions WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<PromotionEntity>>

    @Query("CREATE INDEX IF NOT EXISTS idx_promotions_active ON promotions(isActive, isDeleted)")
    suspend fun indexActive()
}