package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scale_items")
data class ScaleItemEntity(
    @PrimaryKey
    val id: String = "",
    val productId: String = "",
    val unit: String = "KG",
    val conversionFactor: Double = 1.0,
    val isActive: Boolean = true,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)