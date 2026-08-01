package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "approval_requests")
data class ApprovalRequestEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val requestType: String = "",
    val entityId: String = "",
    val entityType: String = "",
    val requestedByUserId: String = "",
    val currentApproverUserId: String? = null,
    val status: String = "PENDING",
    val approvalChainJson: String? = null,
    val comments: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val resolvedAt: Long? = null,
    val syncStatus: String = "PENDING"
)