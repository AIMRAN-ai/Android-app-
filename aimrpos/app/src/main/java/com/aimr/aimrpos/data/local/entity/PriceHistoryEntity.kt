package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "price_history")
data class PriceHistoryEntity(
    @PrimaryKey
    val id: String = "",
    val productId: String = "",
    val oldPrice: Double = 0.0,
    val newPrice: Double = 0.0,
    val changedBy: String = "",
    val changedAt: Long = 0L,
    val reason: String? = null,
    val syncStatus: String = "PENDING"
)