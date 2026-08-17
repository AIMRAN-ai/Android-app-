package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "price_tiers")
data class PriceTierEntity(
    @PrimaryKey
    val id: String = "",
    val productId: String = "",
    val minQty: Double = 0.0,
    val price: Double = 0.0,
    val customerType: String = "RETAIL",
    val effectiveFrom: Long = 0L,
    val effectiveTo: Long? = null,
    val isActive: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)