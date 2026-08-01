package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val userName: String = "",
    val action: String = "",
    val entityType: String = "",
    val entityId: String = "",
    val oldValue: String? = null,
    val newValue: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val deviceId: String = "",
    val ipAddress: String? = null
)