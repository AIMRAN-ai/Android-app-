package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.ReturnInvoiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReturnInvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(returnInvoice: ReturnInvoiceEntity)

    @Query("SELECT * FROM returns WHERE id = :id")
    suspend fun getById(id: String): ReturnInvoiceEntity?

    @Query("SELECT * FROM returns WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<ReturnInvoiceEntity>>

    @Query("SELECT * FROM returns WHERE originalInvoiceId = :invoiceId AND isDeleted = 0")
    fun getByOriginalInvoice(invoiceId: String): Flow<List<ReturnInvoiceEntity>>

    @Query("SELECT * FROM returns WHERE originalPoId = :poId AND isDeleted = 0")
    fun getByOriginalPo(poId: String): Flow<List<ReturnInvoiceEntity>>

    @Query("SELECT * FROM returns WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<ReturnInvoiceEntity>>

    @Query("UPDATE returns SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_return_sync_status ON returns(syncStatus)")
    suspend fun indexSyncStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_return_original_inv ON returns(originalInvoiceId)")
    suspend fun indexOriginalInvoice()

    @Query("CREATE INDEX IF NOT EXISTS idx_return_original_po ON returns(originalPoId)")
    suspend fun indexOriginalPo()
}