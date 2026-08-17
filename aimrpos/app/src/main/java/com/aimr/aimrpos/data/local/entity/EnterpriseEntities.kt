package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class RoleEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val nameUr: String? = null,
    val description: String = "",
    val permissionsJson: String = "[]",
    val isSystemRole: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "permissions")
data class PermissionEntity(
    @PrimaryKey
    val id: String = "",
    val key: String = "",
    val category: String = "",
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)

@Entity(tableName = "user_roles")
data class UserRoleEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val roleId: String = "",
    val assignedByUserId: String = "",
    val assignedAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val code: String = "",
    val name: String = "",
    val symbol: String = "",
    val exchangeRate: Double = 1.0,
    val isBaseCurrency: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "workflow_rules")
data class WorkflowRuleEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val entityType: String = "",
    val action: String = "",
    val conditionsJson: String = "{}",
    val autoApprove: Boolean = false,
    val notifyUsersJson: String = "[]",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "sales_forecasts")
data class SalesForecastEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val productId: String? = null,
    val categoryId: String? = null,
    val forecastDate: Long = 0L,
    val predictedQty: Double = 0.0,
    val predictedRevenue: Double = 0.0,
    val confidence: Float = 0f,
    val modelVersion: String = "v1",
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "analytics_snapshots")
data class AnalyticsSnapshotEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
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
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "INFO",
    val relatedEntityType: String? = null,
    val relatedEntityId: String? = null,
    val isRead: Boolean = false,
    val readAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val rate: Double = 0.0,
    val validFrom: Long = System.currentTimeMillis(),
    val validTo: Long? = null,
    val source: String = "MANUAL",
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING"
)

@Entity(tableName = "business_units")
data class BusinessUnitEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
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
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING"
)