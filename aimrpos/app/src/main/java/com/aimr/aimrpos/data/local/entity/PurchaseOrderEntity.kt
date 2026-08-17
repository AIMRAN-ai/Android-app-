package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase_orders")
data class PurchaseOrderEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val poNumber: String = "",
    val supplierId: String = "",
    val locationId: String = "",
    val items: String = "",
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val total: Double = 0.0,
    val status: String = "DRAFT",
    val createdByUserId: String = "",
    val approvedByUserId: String? = null,
    val approvedAt: Long? = null,
    val expectedDeliveryDate: Long? = null,
    val sourceDocumentId: String? = null,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)