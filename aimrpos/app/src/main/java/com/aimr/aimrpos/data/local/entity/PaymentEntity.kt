package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val invoiceId: String? = null,
    val customerId: String? = null,
    val supplierId: String? = null,
    val amount: Double = 0.0,
    val method: String = "CASH",
    val reference: String? = null,
    val notes: String? = null,
    val createdByUserId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)