package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loyalty_rules")
data class LoyaltyRuleEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val pointsPerAmount: Double = 1.0,
    val minPurchaseAmount: Double = 0.0,
    val pointsExpiryDays: Int? = null,
    val applicableCustomerType: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)