package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "purchase_order_items",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["poId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchaseOrderItemEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val poId: String = "",
    val productId: String = "",
    val quantityOrdered: Double = 0.0,
    val quantityReceived: Double = 0.0,
    val unitCost: Double = 0.0,
    val lineTotal: Double = 0.0,
    val updatedAt: Long = System.currentTimeMillis()
)