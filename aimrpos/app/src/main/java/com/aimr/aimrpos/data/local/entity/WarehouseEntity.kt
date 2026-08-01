package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val businessId: String = "",
    val name: String = "",
    val address: String? = null,
    val phone: String? = null,
    val managerUserId: String? = null,
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,
    val isWarehouse: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)