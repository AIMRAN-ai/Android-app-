package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.PurchaseOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(po: PurchaseOrderEntity)

    @Query("SELECT * FROM purchase_orders WHERE id = :id")
    suspend fun getById(id: String): PurchaseOrderEntity?

    @Query("SELECT * FROM purchase_orders WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<PurchaseOrderEntity>>

    @Query("SELECT * FROM purchase_orders WHERE status = :status AND isDeleted = 0")
    fun getByStatus(status: String): Flow<List<PurchaseOrderEntity>>

    @Query("SELECT * FROM purchase_orders WHERE supplierId = :supplierId AND isDeleted = 0")
    fun getBySupplier(supplierId: String): Flow<List<PurchaseOrderEntity>>

    @Query("SELECT * FROM purchase_orders WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<PurchaseOrderEntity>>

    @Query("UPDATE purchase_orders SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}