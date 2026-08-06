package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loyalty_customers")
data class LoyaltyCustomerEntity(
    @PrimaryKey
    val id: String = "",
    val customerId: String = "",
    val pointsBalance: Int = 0,
    val totalEarned: Int = 0,
    val totalRedeemed: Int = 0,
    val tier: String = "BRONZE",
    val joinedAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)