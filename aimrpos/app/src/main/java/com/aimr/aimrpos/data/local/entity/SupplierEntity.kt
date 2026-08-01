package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val nameUr: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val cnic: String? = null,
    val taxNumber: String? = null,
    val creditLimit: Double = 0.0,
    val creditBalance: Double = 0.0,
    val paymentTerms: String? = null,
    val bankAccount: String? = null,
    val bankName: String? = null,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)