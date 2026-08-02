package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val invoiceNumber: String = "",
    val customerId: String? = null,
    val customerName: String = "",
    val customerPhone: String? = null,
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val paymentStatus: String = "PENDING",
    val paymentMethod: String = "CASH",
    val createdByUserId: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)