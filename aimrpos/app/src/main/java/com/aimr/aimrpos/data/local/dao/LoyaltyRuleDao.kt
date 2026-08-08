package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.LoyaltyRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoyaltyRuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rule: LoyaltyRuleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(rules: List<LoyaltyRuleEntity>)

    @Query("SELECT * FROM loyalty_rules WHERE id = :id")
    suspend fun getById(id: String): LoyaltyRuleEntity?

    @Query("SELECT * FROM loyalty_rules WHERE isDeleted = 0 AND isActive = 1")
    fun getAllActive(): Flow<List<LoyaltyRuleEntity>>

    @Query("SELECT * FROM loyalty_rules WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAll(): Flow<List<LoyaltyRuleEntity>>

    @Query("UPDATE loyalty_rules SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("SELECT * FROM loyalty_rules WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<LoyaltyRuleEntity>>
}