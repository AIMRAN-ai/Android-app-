package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "returns")
data class ReturnInvoiceEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val returnNumber: String = "",
    val originalInvoiceId: String? = null,
    val originalPoId: String? = null,
    val customerId: String? = null,
    val supplierId: String? = null,
    val items: String = "",
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val discount: Double = 0.0,
    val refundAmount: Double = 0.0,
    val total: Double = 0.0,
    val reason: String = "",
    val status: String = "PENDING",
    val approvedByUserId: String? = null,
    val createdByUserId: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)