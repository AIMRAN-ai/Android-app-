package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.PriceHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: PriceHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(history: List<PriceHistoryEntity>)

    @Query("SELECT * FROM price_history WHERE productId = :productId ORDER BY changedAt DESC")
    fun getByProduct(productId: String): Flow<List<PriceHistoryEntity>>

    @Query("SELECT * FROM price_history WHERE changedBy = :userId ORDER BY changedAt DESC")
    fun getByUser(userId: String): Flow<List<PriceHistoryEntity>>

    @Query("SELECT * FROM price_history WHERE changedAt BETWEEN :from AND :to ORDER BY changedAt DESC")
    fun getBetween(from: Long, to: Long): Flow<List<PriceHistoryEntity>>

    @Query("SELECT * FROM price_history WHERE id = :id")
    suspend fun getById(id: String): PriceHistoryEntity?

    @Query("SELECT * FROM price_history ORDER BY changedAt DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<PriceHistoryEntity>>

    @Query("DELETE FROM price_history WHERE productId = :productId")
    suspend fun deleteForProduct(productId: String)

    @Query("UPDATE price_history SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_price_history_product ON price_history(productId)")
    suspend fun indexProductId()

    @Query("CREATE INDEX IF NOT EXISTS idx_price_history_changed_at ON price_history(changedAt)")
    suspend fun indexChangedAt()
}