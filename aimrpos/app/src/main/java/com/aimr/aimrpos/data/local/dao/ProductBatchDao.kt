package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.ProductBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductBatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(batch: ProductBatchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(batches: List<ProductBatchEntity>)

    @Query("SELECT * FROM product_batches WHERE id = :id")
    suspend fun getById(id: String): ProductBatchEntity?

    @Query("SELECT * FROM product_batches WHERE productId = :productId AND isDeleted = 0 ORDER BY expiryDate ASC")
    fun getByProduct(productId: String): Flow<List<ProductBatchEntity>>

    @Query("SELECT * FROM product_batches WHERE productId = :productId AND expiryDate IS NOT NULL AND expiryDate <= :before AND isDeleted = 0 ORDER BY expiryDate ASC")
    fun getExpiringBefore(productId: String, before: Long): Flow<List<ProductBatchEntity>>

    @Query("SELECT * FROM product_batches WHERE expiryDate IS NOT NULL AND expiryDate <= :before AND isDeleted = 0 ORDER BY expiryDate ASC")
    fun getAllExpiringBefore(before: Long): Flow<List<ProductBatchEntity>>

    @Query("SELECT * FROM product_batches WHERE isDeleted = 0 ORDER BY expiryDate ASC")
    fun getAll(): Flow<List<ProductBatchEntity>>

    @Query("DELETE FROM product_batches WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM product_batches WHERE productId = :productId")
    suspend fun deleteForProduct(productId: String)

    @Query("UPDATE product_batches SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("SELECT * FROM product_batches WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<ProductBatchEntity>>

    @Query("CREATE INDEX IF NOT EXISTS idx_product_batches_product ON product_batches(productId)")
    suspend fun indexProductId()

    @Query("CREATE INDEX IF NOT EXISTS idx_product_batches_expiry ON product_batches(expiryDate)")
    suspend fun indexExpiryDate()
}