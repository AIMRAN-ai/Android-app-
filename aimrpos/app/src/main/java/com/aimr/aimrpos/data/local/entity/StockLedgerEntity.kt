package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "stock_ledger",
    foreignKeys = [
        ForeignKey(
            entity = com.aimr.aimrpos.data.local.entity.ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StockLedgerEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val productId: String = "",
    val locationId: String = "",
    val movementType: String = "IN",
    val quantity: Double = 0.0,
    val referenceType: String = "",
    val referenceId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)