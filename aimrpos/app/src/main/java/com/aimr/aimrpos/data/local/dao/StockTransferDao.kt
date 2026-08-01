package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.StockTransferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockTransferDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transfer: StockTransferEntity)

    @Query("SELECT * FROM stock_transfers WHERE id = :id")
    suspend fun getById(id: String): StockTransferEntity?

    @Query("SELECT * FROM stock_transfers WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAll(): Flow<List<StockTransferEntity>>

    @Query("SELECT * FROM stock_transfers WHERE status = :status AND isDeleted = 0")
    fun getByStatus(status: String): Flow<List<StockTransferEntity>>

    @Query("SELECT * FROM stock_transfers WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<StockTransferEntity>>

    @Query("UPDATE stock_transfers SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_stock_sync_status ON stock_transfers(syncStatus)")
    suspend fun indexSyncStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_stock_status ON stock_transfers(status)")
    suspend fun indexStatus()
}