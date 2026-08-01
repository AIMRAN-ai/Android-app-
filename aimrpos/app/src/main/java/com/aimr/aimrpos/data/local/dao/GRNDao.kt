package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.GRNEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GRNDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(grn: GRNEntity)

    @Query("SELECT * FROM grns WHERE id = :id")
    suspend fun getById(id: String): GRNEntity?

    @Query("SELECT * FROM grns WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<GRNEntity>>

    @Query("SELECT * FROM grns WHERE status = :status AND isDeleted = 0")
    fun getByStatus(status: String): Flow<List<GRNEntity>>

    @Query("SELECT * FROM grns WHERE purchaseOrderId = :poId AND isDeleted = 0")
    fun getByPurchaseOrder(poId: String): Flow<List<GRNEntity>>

    @Query("SELECT * FROM grns WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<GRNEntity>>

    @Query("UPDATE grns SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}