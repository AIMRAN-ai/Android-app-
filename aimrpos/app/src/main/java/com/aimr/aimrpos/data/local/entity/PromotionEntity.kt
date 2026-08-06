package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promotions")
data class PromotionEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val type: String = "PERCENTAGE",
    val value: Double = 0.0,
    val minPurchaseAmount: Double = 0.0,
    val maxDiscountAmount: Double? = null,
    val applicableProductIds: String? = null,
    val applicableCategoryIds: String? = null,
    val customerType: String? = null,
    val startDate: Long = 0L,
    val endDate: Long? = null,
    val isActive: Boolean = true,
    val usageLimit: Int? = null,
    val usageCount: Int = 0,
    val createdBy: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)