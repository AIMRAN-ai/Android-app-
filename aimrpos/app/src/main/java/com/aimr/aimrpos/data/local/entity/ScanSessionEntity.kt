package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_sessions")
data class ScanSessionEntity(
    @PrimaryKey
    val id: String = "",
    val businessId: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val thumbnailPath: String? = null,
    val ocrRawText: String? = null,
    val extractionStatus: String = "PENDING",
    val scannedByUserId: String = "",
    val scannedAt: Long = 0L,
    val locationId: String? = null,
    val confidence: Float = 0f,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)