package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_sessions")
data class ScanSessionEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val sessionName: String = "",
    val folderPath: String = "",
    val documentTypeFilter: String = "ALL",
    val processingMode: String = "SIMPLE",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val totalPages: Int = 0,
    val processedPages: Int = 0,
    val isCompleted: Boolean = false,
    val createdByUserId: String = "",
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "scanned_documents")
data class DocumentVaultEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val businessId: String = "",
    val sessionId: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val thumbnailPath: String? = null,
    val ocrRawText: String? = null,
    val extractedFieldsCsv: String? = null,
    val processingMode: String = "SIMPLE",
    val extractionStatus: String = "PENDING",
    val linkedRecordType: String? = null,
    val linkedRecordId: String? = null,
    val scannedByUserId: String = "",
    val scannedAt: Long = System.currentTimeMillis(),
    val locationId: String? = null,
    val confidence: Float = 0.0f,
    val pageNumber: Int = 1,
    val totalPages: Int = 1,
    val uploadedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)