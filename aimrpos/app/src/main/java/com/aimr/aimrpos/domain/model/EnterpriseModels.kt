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
data class AuditLog(
    val id: String = "",
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
    val timestamp: Long = 0L
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
data class Location(
    val id: String = "",
    val businessId: String = "",
    val name: String = "",
    val address: String? = null,
    val phone: String? = null,
    val managerUserId: String? = null,
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,
    val isWarehouse: Boolean = true,
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