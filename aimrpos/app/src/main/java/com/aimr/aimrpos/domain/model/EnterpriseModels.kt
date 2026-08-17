package com.aimr.aimrpos.domain.model

import androidx.compose.runtime.Stable

@Stable
data class PurchaseOrder(
    val id: String = "",
    val poNumber: String = "",
    val supplierId: String = "",
    val locationId: String = "",
    val items: List<POLineItem> = emptyList(),
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val total: Double = 0.0,
    val status: String = "DRAFT",
    val createdByUserId: String = "",
    val approvedByUserId: String? = null,
    val approvedAt: Long? = null,
    val expectedDeliveryDate: Long? = null,
    val sourceDocumentId: String? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class POLineItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val taxRate: Double = 0.0,
    val lineTotal: Double = 0.0
)

@Stable
data class GRN(
    val id: String = "",
    val grnNumber: String = "",
    val purchaseOrderId: String = "",
    val supplierId: String = "",
    val locationId: String = "",
    val items: List<GRNLineItem> = emptyList(),
    val totalQty: Double = 0.0,
    val totalAmount: Double = 0.0,
    val status: String = "PENDING",
    val receivedByUserId: String = "",
    val createdByUserId: String = "",
    val receivedAt: Long = 0L,
    val discrepancyNotes: String? = null,
    val sourceDocumentId: String? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class GRNLineItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val lineTotal: Double = 0.0
)

@Stable
data class ReturnInvoice(
    val id: String = "",
    val returnNumber: String = "",
    val originalInvoiceId: String? = null,
    val originalPoId: String? = null,
    val customerId: String? = null,
    val supplierId: String? = null,
    val items: List<ReturnLineItem> = emptyList(),
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val discount: Double = 0.0,
    val refundAmount: Double = 0.0,
    val total: Double = 0.0,
    val reason: String = "",
    val status: String = "PENDING",
    val approvedByUserId: String? = null,
    val createdByUserId: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class ReturnLineItem(
    val invoiceItemId: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val lineTotal: Double = 0.0
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

@Stable
data class DashboardWidget(
    val id: String = "",
    val widgetType: String = "",
    val title: String = "",
    val positionX: Int = 0,
    val positionY: Int = 0,
    val width: Int = 1,
    val height: Int = 1,
    val configJson: String = "{}",
    val isVisible: Boolean = true,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class DocumentVault(
    val id: String = "",
    val businessId: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val thumbnailPath: String? = null,
    val ocrRawText: String? = null,
    val extractionStatus: String = "PENDING",
    val linkedRecordType: String? = null,
    val linkedRecordId: String? = null,
    val scannedByUserId: String = "",
    val scannedAt: Long = 0L,
    val locationId: String? = null,
    val confidence: Float = 0.0f,
    val uploadedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class PriceTier(
    val id: String = "",
    val productId: String = "",
    val minQty: Double = 0.0,
    val price: Double = 0.0,
    val customerType: String = "RETAIL",
    val effectiveFrom: Long = 0L,
    val effectiveTo: Long? = null,
    val isActive: Boolean = true,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class PriceHistory(
    val id: String = "",
    val productId: String = "",
    val oldPrice: Double = 0.0,
    val newPrice: Double = 0.0,
    val changedBy: String = "",
    val changedAt: Long = 0L,
    val reason: String? = null,
    val syncStatus: String = "PENDING"
)

@Stable
data class Promotion(
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val type: String = "PERCENTAGE",
    val value: Double = 0.0,
    val minPurchaseAmount: Double = 0.0,
    val maxDiscountAmount: Double? = null,
    val applicableProductIds: List<String> = emptyList(),
    val applicableCategoryIds: List<String> = emptyList(),
    val customerType: String? = null,
    val startDate: Long = 0L,
    val endDate: Long? = null,
    val isActive: Boolean = true,
    val usageLimit: Int? = null,
    val usageCount: Int = 0,
    val createdBy: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class ScaleItem(
    val id: String = "",
    val productId: String = "",
    val unit: String = "KG",
    val conversionFactor: Double = 1.0,
    val isActive: Boolean = true,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class ProductBatch(
    val id: String = "",
    val productId: String = "",
    val batchNumber: String = "",
    val quantity: Double = 0.0,
    val manufacturingDate: Long? = null,
    val expiryDate: Long? = null,
    val supplierId: String? = null,
    val locationId: String? = null,
    val notes: String? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class LoyaltyCustomer(
    val id: String = "",
    val customerId: String = "",
    val pointsBalance: Int = 0,
    val totalEarned: Int = 0,
    val totalRedeemed: Int = 0,
    val tier: String = "BRONZE",
    val joinedAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class LoyaltyTransaction(
    val id: String = "",
    val loyaltyCustomerId: String = "",
    val customerId: String = "",
    val points: Int = 0,
    val type: String = "EARN",
    val referenceType: String? = null,
    val referenceId: String? = null,
    val description: String? = null,
    val createdAt: Long = 0L,
    val syncStatus: String = "PENDING"
)

@Stable
data class LoyaltyRule(
    val id: String = "",
    val name: String = "",
    val pointsPerAmount: Double = 1.0,
    val minPurchaseAmount: Double = 0.0,
    val pointsExpiryDays: Int? = null,
    val applicableCustomerType: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class ApprovalRequest(
    val id: String = "",
    val requestType: String = "",
    val entityId: String = "",
    val entityType: String = "",
    val requestedByUserId: String = "",
    val currentApproverUserId: String? = null,
    val status: String = "PENDING",
    val approvalChainJson: String? = null,
    val comments: String? = null,
    val createdAt: Long = 0L,
    val resolvedAt: Long? = null,
    val syncStatus: String = "PENDING"
)

@Stable
data class Supplier(
    val id: String = "",
    val businessId: String = "",
    val name: String = "",
    val nameUr: String? = null,
    val contactPerson: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val paymentTerms: String? = null,
    val creditLimit: Double = 0.0,
    val currentBalance: Double = 0.0,
    val taxNumber: String? = null,
    val bankAccount: String? = null,
    val bankName: String? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class StockTransfer(
    val id: String = "",
    val transferNumber: String = "",
    val fromLocationId: String = "",
    val toLocationId: String = "",
    val productId: String = "",
    val quantity: Double = 0.0,
    val status: String = "PENDING",
    val requestedByUserId: String = "",
    val approvedByUserId: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

@Stable
data class StockLedgerEntry(
    val id: String = "",
    val productId: String = "",
    val locationId: String = "",
    val movementType: String = "IN",
    val quantity: Double = 0.0,
    val referenceType: String = "",
    val referenceId: String? = null,
    val createdAt: Long = 0L
)

@Stable
data class Payment(
    val id: String = "",
    val invoiceId: String? = null,
    val customerId: String? = null,
    val supplierId: String? = null,
    val amount: Double = 0.0,
    val method: String = "CASH",
    val reference: String? = null,
    val notes: String? = null,
    val createdByUserId: String = "",
    val createdAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)