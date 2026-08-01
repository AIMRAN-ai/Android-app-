package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_transfers")
data class StockTransferEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val transferNumber: String = "",
    val fromWarehouseId: String = "",
    val toWarehouseId: String = "",
    val productId: String = "",
    val quantity: Double = 0.0,
    val status: String = "PENDING",
    val transferredByUserId: String = "",
    val approvedByUserId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)