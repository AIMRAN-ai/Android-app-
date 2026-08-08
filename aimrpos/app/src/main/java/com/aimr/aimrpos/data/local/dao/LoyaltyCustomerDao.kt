package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.LoyaltyCustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoyaltyCustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(customer: LoyaltyCustomerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(customers: List<LoyaltyCustomerEntity>)

    @Query("SELECT * FROM loyalty_customers WHERE id = :id")
    suspend fun getById(id: String): LoyaltyCustomerEntity?

    @Query("SELECT * FROM loyalty_customers WHERE customerId = :customerId AND isDeleted = 0")
    suspend fun getByCustomerId(customerId: String): LoyaltyCustomerEntity?

    @Query("SELECT * FROM loyalty_customers WHERE isDeleted = 0 ORDER BY pointsBalance DESC")
    fun getAll(): Flow<List<LoyaltyCustomerEntity>>

    @Query("SELECT * FROM loyalty_customers WHERE tier = :tier AND isDeleted = 0")
    fun getByTier(tier: String): Flow<List<LoyaltyCustomerEntity>>

    @Query("UPDATE loyalty_customers SET pointsBalance = pointsBalance + :points, totalEarned = totalEarned + :points, updatedAt = :updatedAt WHERE customerId = :customerId")
    suspend fun addPoints(customerId: String, points: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE loyalty_customers SET pointsBalance = pointsBalance - :points, totalRedeemed = totalRedeemed + :points, updatedAt = :updatedAt WHERE customerId = :customerId")
    suspend fun redeemPoints(customerId: String, points: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE loyalty_customers SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("SELECT * FROM loyalty_customers WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<LoyaltyCustomerEntity>>

    @Query("CREATE INDEX IF NOT EXISTS idx_loyalty_customers_customer ON loyalty_customers(customerId)")
    suspend fun indexCustomerId()
}