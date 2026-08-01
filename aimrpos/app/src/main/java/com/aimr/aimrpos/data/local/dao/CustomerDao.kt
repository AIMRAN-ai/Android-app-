package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getById(id: String): CustomerEntity?

    @Query("SELECT * FROM customers WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAll(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE isDeleted = 0 AND creditBalance > 0 ORDER BY name ASC")
    fun getWithCredit(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<CustomerEntity>>

    @Query("UPDATE customers SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_customers_sync_status ON customers(syncStatus)")
    suspend fun indexSyncStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_customers_credit ON customers(creditBalance)")
    suspend fun indexCredit()
}