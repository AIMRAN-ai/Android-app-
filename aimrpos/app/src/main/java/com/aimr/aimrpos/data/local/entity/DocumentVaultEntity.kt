package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_documents")
data class DocumentVaultEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val businessId: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val thumbnailPath: String? = null,
    val ocrRawText: String? = null,
    val extractionStatus: String = "PENDING",
    val linkedRecordType: String? = null,
    val linkedRecordId: String? = null,
    val scannedByUserId: String = "",
    val scannedAt: Long = System.currentTimeMillis(),
    val locationId: String? = null,
    val confidence: Float = 0.0f,
    val uploadedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)