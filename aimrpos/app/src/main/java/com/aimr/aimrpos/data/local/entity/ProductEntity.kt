package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val nameUr: String? = null,
    val sku: String = "",
    val barcode: String? = null,
    val qrCode: String? = null,
    val productType: String = "PHYSICAL",
    val categoryId: String = "",
    val costPrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val stockQty: Double = 0.0,
    val unit: String = "pcs",
    val lowStockThreshold: Double = 5.0,
    val imagePath: String? = null,
    val taxRate: Double = 17.0,
    val discountPercent: Double = 0.0,
    val isActive: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)