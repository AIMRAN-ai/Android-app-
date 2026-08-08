package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.LoyaltyTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoyaltyTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: LoyaltyTransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<LoyaltyTransactionEntity>)

    @Query("SELECT * FROM loyalty_transactions WHERE id = :id")
    suspend fun getById(id: String): LoyaltyTransactionEntity?

    @Query("SELECT * FROM loyalty_transactions WHERE loyaltyCustomerId = :loyaltyCustomerId ORDER BY createdAt DESC")
    fun getByLoyaltyCustomer(loyaltyCustomerId: String): Flow<List<LoyaltyTransactionEntity>>

    @Query("SELECT * FROM loyalty_transactions WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getByCustomer(customerId: String): Flow<List<LoyaltyTransactionEntity>>

    @Query("SELECT * FROM loyalty_transactions WHERE type = :type ORDER BY createdAt DESC")
    fun getByType(type: String): Flow<List<LoyaltyTransactionEntity>>

    @Query("SELECT * FROM loyalty_transactions WHERE createdAt BETWEEN :from AND :to ORDER BY createdAt DESC")
    fun getBetween(from: Long, to: Long): Flow<List<LoyaltyTransactionEntity>>

    @Query("UPDATE loyalty_transactions SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("SELECT * FROM loyalty_transactions WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<LoyaltyTransactionEntity>>

    @Query("CREATE INDEX IF NOT EXISTS idx_loyalty_transactions_customer ON loyalty_transactions(loyaltyCustomerId)")
    suspend fun indexLoyaltyCustomerId()
}