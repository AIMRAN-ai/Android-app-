package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.InvoiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(invoice: InvoiceEntity)

    @Query("SELECT * FROM invoices WHERE id = :id")
    suspend fun getById(id: String): InvoiceEntity?

    @Query("SELECT * FROM invoices WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE isDeleted = 0 AND updatedAt >= :startOfDay ORDER BY updatedAt DESC")
    fun getToday(startOfDay: Long): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE isDeleted = 0 AND customerId = :customerId ORDER BY updatedAt DESC")
    fun getByCustomer(customerId: String): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<InvoiceEntity>>

    @Query("UPDATE invoices SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}