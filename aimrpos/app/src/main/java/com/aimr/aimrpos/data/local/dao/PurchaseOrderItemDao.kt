package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.PurchaseOrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: PurchaseOrderItemEntity)

    @Query("SELECT * FROM purchase_order_items WHERE id = :id")
    suspend fun getById(id: String): PurchaseOrderItemEntity?

    @Query("SELECT * FROM purchase_order_items WHERE poId = :poId")
    fun getByPurchaseOrder(poId: String): Flow<List<PurchaseOrderItemEntity>>

    @Query("SELECT * FROM purchase_order_items WHERE productId = :productId")
    fun getByProduct(productId: String): Flow<List<PurchaseOrderItemEntity>>

    @Query("DELETE FROM purchase_order_items WHERE poId = :poId")
    suspend fun deleteByPurchaseOrder(poId: String)
}