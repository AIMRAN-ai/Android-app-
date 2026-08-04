package com.aimr.aimrpos.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Product(
    val id: String = "",
    val name: String = "",
    val nameUr: String? = null,
    val sku: String = "",
    val barcode: String? = null,
    val qrCode: String? = null,
    val productType: String = "PHYSICAL",
    val categoryId: String = "",
    val costPrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val stockQty: Double = 0.0,
    val unit: String = "pcs",
    val lowStockThreshold: Double = 5.0,
    val imagePath: String? = null,
    val taxRate: Double = 17.0,
    val discountPercent: Double = 0.0,
    val isActive: Boolean = true,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

enum class ProductType(val label: String, val code: String) {
    PHYSICAL("Physical Goods", "PHYSICAL"),
    PERISHABLE("Perishable", "PERISHABLE"),
    DIGITAL("Digital Product", "DIGITAL"),
    SERVICE("Service", "SERVICE"),
    WHOLESALE("Wholesale", "WHOLESALE"),
    RETAIL("Retail", "RETAIL"),
    RAW_MATERIAL("Raw Material", "RAW_MATERIAL"),
    FINISHED_GOODS("Finished Goods", "FINISHED_GOODS")
}

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

@Stable
data class Role(
    val id: String = "",
    val name: String = "",
    val nameUr: String? = null,
    val description: String = "",
    val permissionsJson: String = "[]",
    val isSystemRole: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class Permission(
    val id: String = "",
    val key: String = "",
    val category: String = "",
    val description: String = "",
    val createdAt: Long = 0L,
    val isDeleted: Boolean = false
)

@Stable
data class UserRole(
    val id: String = "",
    val userId: String = "",
    val roleId: String = "",
    val assignedByUserId: String = "",
    val assignedAt: Long = 0L,
    val expiresAt: Long? = null,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class Currency(
    val code: String = "",
    val name: String = "",
    val symbol: String = "",
    val exchangeRate: Double = 1.0,
    val isBaseCurrency: Boolean = false,
    val updatedAt: Long = 0L,
    val syncStatus: String = "PENDING"
)

@Stable
data class ExchangeRate(
    val id: String = "",
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val rate: Double = 0.0,
    val validFrom: Long = 0L,
    val validTo: Long? = null,
    val source: String = "MANUAL",
    val createdAt: Long = 0L,
    val syncStatus: String = "PENDING"
)

@Stable
data class WorkflowRule(
    val id: String = "",
    val name: String = "",
    val entityType: String = "",
    val action: String = "",
    val conditionsJson: String = "{}",
    val autoApprove: Boolean = false,
    val notifyUsersJson: String = "[]",
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val syncStatus: String = "PENDING"
)

@Stable
data class SalesForecast(
    val id: String = "",
    val productId: String? = null,
    val categoryId: String? = null,
    val forecastDate: Long = 0L,
    val predictedQty: Double = 0.0,
    val predictedRevenue: Double = 0.0,
    val confidence: Float = 0f,
    val modelVersion: String = "v1",
    val createdAt: Long = 0L,
    val syncStatus: String = "PENDING"
)

@Stable
data class AnalyticsSnapshot(
    val id: String = "",
    val snapshotDate: Long = 0L,
    val period: String = "DAILY",
    val totalSales: Double = 0.0,
    val totalOrders: Int = 0,
    val avgOrderValue: Double = 0.0,
    val totalCustomers: Int = 0,
    val totalProducts: Int = 0,
    val lowStockCount: Int = 0,
    val outstandingCredit: Double = 0.0,
    val stockValuation: Double = 0.0,
    val topProductsJson: String = "[]",
    val topCustomersJson: String = "[]",
    val createdAt: Long = 0L,
    val syncStatus: String = "PENDING"
)

@Stable
data class Notification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "INFO",
    val relatedEntityType: String? = null,
    val relatedEntityId: String? = null,
    val isRead: Boolean = false,
    val readAt: Long? = null,
    val createdAt: Long = 0L,
    val syncStatus: String = "PENDING"
)

@Stable
data class BusinessUnit(
    val id: String = "",
    val businessId: String = "",
    val name: String = "",
    val nameUr: String? = null,
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val managerUserId: String = "",
    val timezone: String = "Asia/Karachi",
    val currencyCode: String = "PKR",
    val taxNumber: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val syncStatus: String = "PENDING"
)