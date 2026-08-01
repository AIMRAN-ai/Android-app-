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

    @Query("SELECT * FROM return_invoices WHERE id = :id")
    suspend fun getById(id: String): ReturnInvoiceEntity?

    @Query("SELECT * FROM return_invoices WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<ReturnInvoiceEntity>>

    @Query("SELECT * FROM return_invoices WHERE originalInvoiceId = :invoiceId AND isDeleted = 0")
    fun getByOriginalInvoice(invoiceId: String): Flow<List<ReturnInvoiceEntity>>

    @Query("SELECT * FROM return_invoices WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<ReturnInvoiceEntity>>

    @Query("UPDATE return_invoices SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_return_sync_status ON return_invoices(syncStatus)")
    suspend fun indexSyncStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_return_original ON return_invoices(originalInvoiceId)")
    suspend fun indexOriginalInvoice()
}