package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loyalty_transactions")
data class LoyaltyTransactionEntity(
    @PrimaryKey
    val id: String = "",
    val loyaltyCustomerId: String = "",
    val customerId: String = "",
    val points: Int = 0,
    val type: String = "EARN",
    val referenceType: String? = null,
    val referenceId: String? = null,
    val description: String? = null,
    val createdAt: Long = 0L,
    val syncStatus: String = "PENDING"
)