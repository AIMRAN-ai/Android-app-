package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: String): ProductEntity?

    @Query("SELECT * FROM products WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isDeleted = 0 AND (name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%')")
    fun search(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isDeleted = 0 AND categoryId = :categoryId ORDER BY name ASC")
    fun getByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isDeleted = 0 AND stockQty <= lowStockThreshold")
    fun getLowStock(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE barcode = :barcode AND isDeleted = 0 LIMIT 1")
    suspend fun getByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM products WHERE qrCode = :qrCode AND isDeleted = 0 LIMIT 1")
    suspend fun getByQrCode(qrCode: String): ProductEntity?

    @Query("SELECT * FROM products WHERE productType = :type AND isDeleted = 0 ORDER BY name ASC")
    fun getByProductType(type: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<ProductEntity>>

    @Query("UPDATE products SET stockQty = stockQty - :quantity WHERE id = :productId AND stockQty >= :quantity")
    suspend fun deductStock(productId: String, quantity: Double): Int

    @Query("UPDATE products SET stockQty = stockQty + :quantity WHERE id = :productId")
    suspend fun addStock(productId: String, quantity: Double)

    @Query("UPDATE products SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_products_sync_status ON products(syncStatus)")
    suspend fun indexSyncStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_products_category ON products(categoryId)")
    suspend fun indexCategory()

    @Query("CREATE INDEX IF NOT EXISTS idx_products_barcode ON products(barcode)")
    suspend fun indexBarcode()

    @Query("CREATE INDEX IF NOT EXISTS idx_products_sku ON products(sku)")
    suspend fun indexSku()
}