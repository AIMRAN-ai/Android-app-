package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_log")
data class AuditLogEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val businessId: String = "",
    val userId: String = "",
    val userName: String = "",
    val action: String = "",
    val entityType: String = "",
    val entityId: String = "",
    val oldValueJson: String? = null,
    val newValueJson: String? = null,
    val deviceId: String = "",
    val ipAddress: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)