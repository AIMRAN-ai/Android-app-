package com.aimr.aimrpos.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Product(
    val id: String = "",
    val name: String = "",
    val nameUr: String? = null,
    val sku: String = "",
    val barcode: String? = null,
    val categoryId: String = "",
    val costPrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val stockQty: Double = 0.0,
    val unit: String = "pcs",
    val lowStockThreshold: Double = 5.0,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class Invoice(
    val id: String = "",
    val invoiceNumber: String = "",
    val customerId: String? = null,
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val paymentStatus: String = "PENDING",
    val paymentMethod: String? = null,
    val createdByUserId: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class InvoiceItem(
    val id: String = "",
    val invoiceId: String = "",
    val productId: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val taxRate: Double = 0.0,
    val lineTotal: Double = 0.0
)

@Stable
data class Customer(
    val id: String = "",
    val name: String = "",
    val phone: String? = null,
    val address: String? = null,
    val creditBalance: Double = 0.0,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class User(
    val id: String = "",
    val businessId: String = "",
    val name: String = "",
    val role: String = "STAFF",
    val phone: String? = null,
    val pinHash: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class Business(
    val id: String = "",
    val name: String = "",
    val address: String? = null,
    val taxNumber: String? = null,
    val currency: String = "PKR",
    val subscriptionTier: String = "FREE",
    val subscriptionExpiry: Long? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class Category(
    val id: String = "",
    val name: String = "",
    val nameUr: String? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)