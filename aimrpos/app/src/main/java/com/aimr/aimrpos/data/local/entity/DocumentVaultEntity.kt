package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document_vault")
data class DocumentVaultEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val documentType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val thumbnailPath: String? = null,
    val ocrText: String? = null,
    val extractedFields: String? = null,
    val linkedEntityId: String? = null,
    val linkedEntityType: String? = null,
    val confidence: Float = 0.0f,
    val uploadedByUserId: String = "",
    val uploadedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)