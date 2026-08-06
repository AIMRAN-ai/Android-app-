package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.ScaleItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScaleItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(scaleItem: ScaleItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(scaleItems: List<ScaleItemEntity>)

    @Query("SELECT * FROM scale_items WHERE id = :id")
    suspend fun getById(id: String): ScaleItemEntity?

    @Query("SELECT * FROM scale_items WHERE productId = :productId AND isDeleted = 0")
    suspend fun getByProduct(productId: String): ScaleItemEntity?

    @Query("SELECT * FROM scale_items WHERE isDeleted = 0 AND isActive = 1")
    fun getAllActive(): Flow<List<ScaleItemEntity>>

    @Query("DELETE FROM scale_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM scale_items WHERE productId = :productId")
    suspend fun deleteForProduct(productId: String)

    @Query("UPDATE scale_items SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_scale_items_product ON scale_items(productId)")
    suspend fun indexProductId()
}