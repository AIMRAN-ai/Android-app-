package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_batches")
data class ProductBatchEntity(
    @PrimaryKey
    val id: String = "",
    val productId: String = "",
    val batchNumber: String = "",
    val quantity: Double = 0.0,
    val manufacturingDate: Long? = null,
    val expiryDate: Long? = null,
    val supplierId: String? = null,
    val locationId: String? = null,
    val notes: String? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)