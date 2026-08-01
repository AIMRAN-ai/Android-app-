package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grns")
data class GRNEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val grnNumber: String = "",
    val purchaseOrderId: String = "",
    val supplierId: String = "",
    val locationId: String = "",
    val items: String = "",
    val totalQty: Double = 0.0,
    val totalAmount: Double = 0.0,
    val status: String = "PENDING",
    val receivedByUserId: String = "",
    val createdByUserId: String = "",
    val receivedAt: Long = System.currentTimeMillis(),
    val discrepancyNotes: String? = null,
    val sourceDocumentId: String? = null,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)