package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.PriceTierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceTierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(tier: PriceTierEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tiers: List<PriceTierEntity>)

    @Query("SELECT * FROM price_tiers WHERE id = :id")
    suspend fun getById(id: String): PriceTierEntity?

    @Query("SELECT * FROM price_tiers WHERE productId = :productId AND isDeleted = 0 ORDER BY minQty ASC")
    fun getByProduct(productId: String): Flow<List<PriceTierEntity>>

    @Query("SELECT * FROM price_tiers WHERE productId = :productId AND customerType = :customerType AND :qty >= minQty AND isDeleted = 0 AND isActive = 1 ORDER BY minQty DESC LIMIT 1")
    suspend fun getByProductAndQty(productId: String, qty: Double, customerType: String): PriceTierEntity?

    @Query("SELECT * FROM price_tiers WHERE isDeleted = 0 AND isActive = 1 ORDER BY productId ASC, minQty ASC")
    fun getAllActive(): Flow<List<PriceTierEntity>>

    @Query("DELETE FROM price_tiers WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM price_tiers WHERE productId = :productId")
    suspend fun deleteForProduct(productId: String)

    @Query("DELETE FROM price_tiers WHERE isDeleted = 1")
    suspend fun deleteSoftDeleted()

    @Query("UPDATE price_tiers SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_price_tiers_product ON price_tiers(productId)")
    suspend fun indexProductId()

    @Query("CREATE INDEX IF NOT EXISTS idx_price_tiers_product_qty ON price_tiers(productId, minQty)")
    suspend fun indexProductQty()
}