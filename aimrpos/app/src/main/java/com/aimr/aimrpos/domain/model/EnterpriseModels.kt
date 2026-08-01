package com.aimr.aimrpos.domain.model

data class PurchaseOrder(
    val id: String = "",
    val poNumber: String = "",
    val supplierId: String = "",
    val warehouseId: String = "",
    val items: List<POLineItem> = emptyList(),
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val total: Double = 0.0,
    val status: String = "DRAFT",
    val approvedByUserId: String? = null,
    val approvedAt: Long? = null,
    val expectedDeliveryDate: Long? = null,
    val createdByUserId: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

data class POLineItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val taxRate: Double = 0.0,
    val lineTotal: Double = 0.0
)

data class GRN(
    val id: String = "",
    val grnNumber: String = "",
    val purchaseOrderId: String = "",
    val supplierId: String = "",
    val warehouseId: String = "",
    val items: List<GRNLineItem> = emptyList(),
    val totalQty: Double = 0.0,
    val totalAmount: Double = 0.0,
    val status: String = "PENDING",
    val receivedByUserId: String = "",
    val createdByUserId: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

data class GRNLineItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val lineTotal: Double = 0.0
)

data class ReturnInvoice(
    val id: String = "",
    val returnNumber: String = "",
    val originalInvoiceId: String = "",
    val customerId: String = "",
    val items: List<ReturnLineItem> = emptyList(),
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val reason: String = "",
    val status: String = "PENDING",
    val approvedByUserId: String? = null,
    val createdByUserId: String = "",
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

data class ReturnLineItem(
    val invoiceItemId: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val lineTotal: Double = 0.0
)

data class AuditLog(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val action: String = "",
    val entityType: String = "",
    val entityId: String = "",
    val oldValue: String? = null,
    val newValue: String? = null,
    val timestamp: Long = 0L,
    val deviceId: String = "",
    val ipAddress: String? = null
)

data class DocumentVault(
    val id: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val thumbnailPath: String? = null,
    val ocrText: String? = null,
    val extractedFields: Map<String, String> = emptyMap(),
    val linkedEntityId: String? = null,
    val linkedEntityType: String? = null,
    val confidence: Float = 0.0f,
    val uploadedByUserId: String = "",
    val uploadedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

data class Warehouse(
    val id: String = "",
    val name: String = "",
    val address: String? = null,
    val phone: String? = null,
    val managerUserId: String? = null,
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

data class ApprovalRequest(
    val id: String = "",
    val requestType: String = "",
    val entityId: String = "",
    val entityType: String = "",
    val requestedByUserId: String = "",
    val approvedByUserId: String? = null,
    val status: String = "PENDING",
    val comments: String? = null,
    val createdAt: Long = 0L,
    val resolvedAt: Long? = null,
    val syncStatus: String = "PENDING"
)

data class Supplier(
    val id: String = "",
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
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

data class StockTransfer(
    val id: String = "",
    val transferNumber: String = "",
    val fromWarehouseId: String = "",
    val toWarehouseId: String = "",
    val productId: String = "",
    val quantity: Double = 0.0,
    val status: String = "PENDING",
    val transferredByUserId: String = "",
    val approvedByUserId: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)

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