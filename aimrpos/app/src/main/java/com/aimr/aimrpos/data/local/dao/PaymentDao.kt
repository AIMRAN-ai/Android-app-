package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE id = :id")
    suspend fun getById(id: String): PaymentEntity?

    @Query("SELECT * FROM payments WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAll(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE invoiceId = :invoiceId AND isDeleted = 0")
    fun getByInvoice(invoiceId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE customerId = :customerId AND isDeleted = 0")
    fun getByCustomer(customerId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE supplierId = :supplierId AND isDeleted = 0")
    fun getBySupplier(supplierId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<PaymentEntity>>

    @Query("UPDATE payments SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_payment_sync_status ON payments(syncStatus)")
    suspend fun indexSyncStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_payment_invoice ON payments(invoiceId)")
    suspend fun indexInvoice()

    @Query("CREATE INDEX IF NOT EXISTS idx_payment_customer ON payments(customerId)")
    suspend fun indexCustomer()
}