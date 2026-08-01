package com.aimr.aimrpos.data.repository

import com.aimr.aimrpos.data.local.dao.ApprovalRequestDao
import com.aimr.aimrpos.data.local.dao.AuditLogDao
import com.aimr.aimrpos.data.local.dao.BusinessDao
import com.aimr.aimrpos.data.local.dao.CategoryDao
import com.aimr.aimrpos.data.local.dao.CustomerDao
import com.aimr.aimrpos.data.local.dao.DocumentVaultDao
import com.aimr.aimrpos.data.local.dao.GRNDao
import com.aimr.aimrpos.data.local.dao.InvoiceDao
import com.aimr.aimrpos.data.local.dao.InvoiceItemDao
import com.aimr.aimrpos.data.local.dao.LocationDao
import com.aimr.aimrpos.data.local.dao.PaymentDao
import com.aimr.aimrpos.data.local.dao.ProductDao
import com.aimr.aimrpos.data.local.dao.PurchaseOrderDao
import com.aimr.aimrpos.data.local.dao.PurchaseOrderItemDao
import com.aimr.aimrpos.data.local.dao.ReturnInvoiceDao
import com.aimr.aimrpos.data.local.dao.StockLedgerDao
import com.aimr.aimrpos.data.local.dao.StockTransferDao
import com.aimr.aimrpos.data.local.dao.SupplierDao
import com.aimr.aimrpos.data.local.entity.AuditLogEntity
import com.aimr.aimrpos.data.local.entity.ApprovalRequestEntity
import com.aimr.aimrpos.data.local.entity.BusinessEntity
import com.aimr.aimrpos.data.local.entity.CategoryEntity
import com.aimr.aimrpos.data.local.entity.CustomerEntity
import com.aimr.aimrpos.data.local.entity.DocumentVaultEntity
import com.aimr.aimrpos.data.local.entity.GRNEntity
import com.aimr.aimrpos.data.local.entity.InvoiceEntity
import com.aimr.aimrpos.data.local.entity.InvoiceItemEntity
import com.aimr.aimrpos.data.local.entity.LocationEntity
import com.aimr.aimrpos.data.local.entity.PaymentEntity
import com.aimr.aimrpos.data.local.entity.ProductEntity
import com.aimr.aimrpos.data.local.entity.PurchaseOrderEntity
import com.aimr.aimrpos.data.local.entity.PurchaseOrderItemEntity
import com.aimr.aimrpos.data.local.entity.ReturnInvoiceEntity
import com.aimr.aimrpos.data.local.entity.StockLedgerEntity
import com.aimr.aimrpos.data.local.entity.StockTransferEntity
import com.aimr.aimrpos.data.local.entity.SupplierEntity
import com.aimr.aimrpos.domain.model.AuditLog
import com.aimr.aimrpos.domain.model.ApprovalRequest
import com.aimr.aimrpos.domain.model.Business
import com.aimr.aimrpos.domain.model.Category
import com.aimr.aimrpos.domain.model.Customer
import com.aimr.aimrpos.domain.model.DocumentVault
import com.aimr.aimrpos.domain.model.GRN
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.InvoiceItem
import com.aimr.aimrpos.domain.model.Location
import com.aimr.aimrpos.domain.model.Payment
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.model.PurchaseOrder
import com.aimr.aimrpos.domain.model.ReturnInvoice
import com.aimr.aimrpos.domain.model.StockLedgerEntry
import com.aimr.aimrpos.domain.model.StockTransfer
import com.aimr.aimrpos.domain.model.Supplier
import com.aimr.aimrpos.domain.model.User
import com.aimr.aimrpos.domain.repository.AuditLogRepository
import com.aimr.aimrpos.domain.repository.ApprovalRequestRepository
import com.aimr.aimrpos.domain.repository.BusinessRepository
import com.aimr.aimrpos.domain.repository.CategoryRepository
import com.aimr.aimrpos.domain.repository.CustomerRepository
import com.aimr.aimrpos.domain.repository.DocumentVaultRepository
import com.aimr.aimrpos.domain.repository.GRNRepository
import com.aimr.aimrpos.domain.repository.InvoiceItemRepository
import com.aimr.aimrpos.domain.repository.InvoiceRepository
import com.aimr.aimrpos.domain.repository.LocationRepository
import com.aimr.aimrpos.domain.repository.PaymentRepository
import com.aimr.aimrpos.domain.repository.ProductRepository
import com.aimr.aimrpos.domain.repository.PurchaseOrderRepository
import com.aimr.aimrpos.domain.repository.ReturnInvoiceRepository
import com.aimr.aimrpos.domain.repository.StockLedgerRepository
import com.aimr.aimrpos.domain.repository.StockTransferRepository
import com.aimr.aimrpos.domain.repository.SupplierRepository
import com.aimr.aimrpos.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(private val dao: ProductDao) : ProductRepository {
    override suspend fun upsert(product: Product) {
        dao.upsert(product.toEntity())
    }
    override suspend fun getById(id: String): Product? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<Product>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun search(query: String): Flow<List<Product>> = dao.search(query).map { it.map { it.toDomain() } }
    override fun getByCategory(categoryId: String): Flow<List<Product>> = dao.getByCategory(categoryId).map { it.map { it.toDomain() } }
    override fun getLowStock(): Flow<List<Product>> = dao.getLowStock().map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class InvoiceRepositoryImpl(private val dao: InvoiceDao) : InvoiceRepository {
    override suspend fun upsert(invoice: Invoice) {
        dao.upsert(invoice.toEntity())
    }
    override suspend fun getById(id: String): Invoice? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<Invoice>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getToday(startOfDay: Long): Flow<List<Invoice>> = dao.getToday(startOfDay).map { it.map { it.toDomain() } }
    override fun getByCustomer(customerId: String): Flow<List<Invoice>> = dao.getByCustomer(customerId).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class InvoiceItemRepositoryImpl(private val dao: InvoiceItemDao) : InvoiceItemRepository {
    override suspend fun insert(item: InvoiceItem) {
        dao.insert(item.toEntity())
    }
    override fun getByInvoice(invoiceId: String): Flow<List<InvoiceItem>> = dao.getByInvoice(invoiceId).map { it.map { it.toDomain() } }
    override suspend fun deleteByInvoice(invoiceId: String) = dao.deleteByInvoice(invoiceId)
}

class CustomerRepositoryImpl(private val dao: CustomerDao) : CustomerRepository {
    override suspend fun upsert(customer: Customer) {
        dao.upsert(customer.toEntity())
    }
    override suspend fun getById(id: String): Customer? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<Customer>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getWithCredit(): Flow<List<Customer>> = dao.getWithCredit().map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class UserRepositoryImpl(private val dao: UserDao) : UserRepository {
    override suspend fun upsert(user: User) {
        dao.upsert(user.toEntity())
    }
    override suspend fun getById(id: String): User? {
        return dao.getById(id)?.toDomain()
    }
    override fun getByBusiness(businessId: String): Flow<List<User>> = dao.getByBusiness(businessId).map { it.map { it.toDomain() } }
    override fun getActive(id: String): Flow<User?> = dao.getActive(id).map { it?.toDomain() }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class BusinessRepositoryImpl(private val dao: BusinessDao) : BusinessRepository {
    override suspend fun upsert(business: Business) {
        dao.upsert(business.toEntity())
    }
    override fun getActive(): Flow<Business?> = dao.getActive().map { it?.toDomain() }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override suspend fun upsert(category: Category) {
        dao.upsert(category.toEntity())
    }
    override fun getAll(): Flow<List<Category>> = dao.getAll().map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class LocationRepositoryImpl(private val dao: LocationDao) : LocationRepository {
    override suspend fun upsert(location: Location) {
        dao.upsert(location.toEntity())
    }
    override suspend fun getById(id: String): Location? {
        return dao.getById(id)?.toDomain()
    }
    override fun getByBusiness(businessId: String): Flow<List<Location>> = dao.getByBusiness(businessId).map { it.map { it.toDomain() } }
    override fun getAll(): Flow<List<Location>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getWarehouses(): Flow<List<Location>> = dao.getWarehouses().map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class PurchaseOrderRepositoryImpl(private val dao: PurchaseOrderDao) : PurchaseOrderRepository {
    override suspend fun upsert(purchaseOrder: PurchaseOrder) {
        dao.upsert(purchaseOrder.toEntity())
    }
    override suspend fun getById(id: String): PurchaseOrder? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<PurchaseOrder>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getByStatus(status: String): Flow<List<PurchaseOrder>> = dao.getByStatus(status).map { it.map { it.toDomain() } }
    override fun getBySupplier(supplierId: String): Flow<List<PurchaseOrder>> = dao.getBySupplier(supplierId).map { it.map { it.toDomain() } }
    override fun getByLocation(locationId: String): Flow<List<PurchaseOrder>> = dao.getByLocation(locationId).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class GRNRepositoryImpl(private val dao: GRNDao) : GRNRepository {
    override suspend fun upsert(grn: com.aimr.aimrpos.domain.model.GRN) {
        dao.upsert(grn.toEntity())
    }
    override suspend fun getById(id: String): com.aimr.aimrpos.domain.model.GRN? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<com.aimr.aimrpos.domain.model.GRN>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getByStatus(status: String): Flow<List<com.aimr.aimrpos.domain.model.GRN>> = dao.getByStatus(status).map { it.map { it.toDomain() } }
    override fun getByPurchaseOrder(poId: String): Flow<List<com.aimr.aimrpos.domain.model.GRN>> = dao.getByPurchaseOrder(poId).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class ReturnInvoiceRepositoryImpl(private val dao: ReturnInvoiceDao) : ReturnInvoiceRepository {
    override suspend fun upsert(returnInvoice: ReturnInvoice) {
        dao.upsert(returnInvoice.toEntity())
    }
    override suspend fun getById(id: String): ReturnInvoice? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<ReturnInvoice>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getByOriginalInvoice(invoiceId: String): Flow<List<ReturnInvoice>> = dao.getByOriginalInvoice(invoiceId).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class SupplierRepositoryImpl(private val dao: SupplierDao) : SupplierRepository {
    override suspend fun upsert(supplier: Supplier) {
        dao.upsert(supplier.toEntity())
    }
    override suspend fun getById(id: String): Supplier? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<Supplier>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getWithBalance(): Flow<List<Supplier>> = dao.getWithBalance().map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class StockTransferRepositoryImpl(private val dao: StockTransferDao) : StockTransferRepository {
    override suspend fun upsert(transfer: StockTransfer) {
        dao.upsert(transfer.toEntity())
    }
    override suspend fun getById(id: String): StockTransfer? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<StockTransfer>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getByStatus(status: String): Flow<List<StockTransfer>> = dao.getByStatus(status).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class StockLedgerRepositoryImpl(private val dao: StockLedgerDao) : StockLedgerRepository {
    override suspend fun insert(entry: StockLedgerEntry) {
        dao.insert(entry.toEntity())
    }
    override fun getByProductAndLocation(productId: String, locationId: String): Flow<List<StockLedgerEntry>> =
        dao.getByProductAndLocation(productId, locationId).map { it.map { it.toDomain() } }
    override fun getByLocation(locationId: String): Flow<List<StockLedgerEntry>> =
        dao.getByLocation(locationId).map { it.map { it.toDomain() } }
    override fun getByProduct(productId: String): Flow<List<StockLedgerEntry>> =
        dao.getByProduct(productId).map { it.map { it.toDomain() } }
}

class DocumentVaultRepositoryImpl(private val dao: DocumentVaultDao) : DocumentVaultRepository {
    override suspend fun upsert(doc: DocumentVault) {
        dao.upsert(doc.toEntity())
    }
    override suspend fun getById(id: String): DocumentVault? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<DocumentVault>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getByEntity(entityId: String): Flow<List<DocumentVault>> = dao.getByEntity(entityId).map { it.map { it.toDomain() } }
    override fun getByType(type: String): Flow<List<DocumentVault>> = dao.getByType(type).map { it.map { it.toDomain() } }
    override fun getByLocation(locationId: String): Flow<List<DocumentVault>> = dao.getByLocation(locationId).map { it.map { it.toDomain() } }
    override fun getByExtractionStatus(status: String): Flow<List<DocumentVault>> = dao.getByExtractionStatus(status).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class ApprovalRequestRepositoryImpl(private val dao: ApprovalRequestDao) : ApprovalRequestRepository {
    override suspend fun upsert(request: ApprovalRequest) {
        dao.upsert(request.toEntity())
    }
    override suspend fun getById(id: String): ApprovalRequest? {
        return dao.getById(id)?.toDomain()
    }
    override fun getPending(): Flow<List<ApprovalRequest>> = dao.getPending().map { it.map { it.toDomain() } }
    override fun getPendingForApprover(userId: String): Flow<List<ApprovalRequest>> = dao.getPendingForApprover(userId).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

class AuditLogRepositoryImpl(private val dao: AuditLogDao) : AuditLogRepository {
    override suspend fun insert(log: AuditLog) {
        dao.insert(log.toEntity())
    }
    override fun getRecent(limit: Int): Flow<List<AuditLog>> = dao.getRecent(limit).map { it.map { it.toDomain() } }
    override fun getByEntity(entityId: String): Flow<List<AuditLog>> = dao.getByEntity(entityId).map { it.map { it.toDomain() } }
    override fun getByUser(userId: String): Flow<List<AuditLog>> = dao.getByUser(userId).map { it.map { it.toDomain() } }
    override fun getByBusiness(businessId: String): Flow<List<AuditLog>> = dao.getByBusiness(businessId).map { it.map { it.toDomain() } }
    override fun getByDateRange(start: Long, end: Long): Flow<List<AuditLog>> = dao.getByDateRange(start, end).map { it.map { it.toDomain() } }
}

class PaymentRepositoryImpl(private val dao: PaymentDao) : PaymentRepository {
    override suspend fun upsert(payment: Payment) {
        dao.upsert(payment.toEntity())
    }
    override suspend fun getById(id: String): Payment? {
        return dao.getById(id)?.toDomain()
    }
    override fun getAll(): Flow<List<Payment>> = dao.getAll().map { it.map { it.toDomain() } }
    override fun getByInvoice(invoiceId: String): Flow<List<Payment>> = dao.getByInvoice(invoiceId).map { it.map { it.toDomain() } }
    override fun getByCustomer(customerId: String): Flow<List<Payment>> = dao.getByCustomer(customerId).map { it.map { it.toDomain() } }
    override fun getBySupplier(supplierId: String): Flow<List<Payment>> = dao.getBySupplier(supplierId).map { it.map { it.toDomain() } }
    override suspend fun updateSyncStatus(id: String, status: String) = dao.updateSyncStatus(id, status)
}

fun LocationEntity.toDomain(): Location = Location(
    id = id, businessId = businessId, name = name, address = address,
    phone = phone, managerUserId = managerUserId,
    locationLatitude = locationLatitude, locationLongitude = locationLongitude,
    isWarehouse = isWarehouse, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun Location.toEntity(): LocationEntity = LocationEntity(
    id = id, businessId = businessId, name = name, address = address,
    phone = phone, managerUserId = managerUserId,
    locationLatitude = locationLatitude, locationLongitude = locationLongitude,
    isWarehouse = isWarehouse, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun PurchaseOrderEntity.toDomain(): PurchaseOrder = PurchaseOrder(
    id = id, poNumber = poNumber, supplierId = supplierId, locationId = locationId,
    items = emptyList(), subtotal = subtotal, taxAmount = taxAmount, total = total,
    status = status, createdByUserId = createdByUserId,
    approvedByUserId = approvedByUserId, approvedAt = approvedAt,
    expectedDeliveryDate = expectedDeliveryDate, sourceDocumentId = sourceDocumentId,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun PurchaseOrder.toEntity(): PurchaseOrderEntity = PurchaseOrderEntity(
    id = id, poNumber = poNumber, supplierId = supplierId, locationId = locationId,
    items = "", subtotal = subtotal, taxAmount = taxAmount, total = total,
    status = status, createdByUserId = createdByUserId,
    approvedByUserId = approvedByUserId, approvedAt = approvedAt,
    expectedDeliveryDate = expectedDeliveryDate, sourceDocumentId = sourceDocumentId,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun GRNEntity.toDomain(): GRN = GRN(
    id = id, grnNumber = grnNumber, purchaseOrderId = purchaseOrderId,
    supplierId = supplierId, locationId = locationId, items = emptyList(),
    totalQty = totalQty, totalAmount = totalAmount, status = status,
    receivedByUserId = receivedByUserId, createdByUserId = createdByUserId,
    receivedAt = receivedAt, discrepancyNotes = discrepancyNotes,
    sourceDocumentId = sourceDocumentId, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun GRN.toEntity(): GRNEntity = GRNEntity(
    id = id, grnNumber = grnNumber, purchaseOrderId = purchaseOrderId,
    supplierId = supplierId, locationId = locationId, items = "",
    totalQty = totalQty, totalAmount = totalAmount, status = status,
    receivedByUserId = receivedByUserId, createdByUserId = createdByUserId,
    receivedAt = receivedAt, discrepancyNotes = discrepancyNotes,
    sourceDocumentId = sourceDocumentId, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun ReturnInvoiceEntity.toDomain(): ReturnInvoice = ReturnInvoice(
    id = id, returnNumber = returnNumber, originalInvoiceId = originalInvoiceId,
    originalPoId = originalPoId, customerId = customerId, supplierId = supplierId,
    items = emptyList(), subtotal = subtotal, taxAmount = taxAmount,
    discount = discount, refundAmount = refundAmount, total = total,
    reason = reason, status = status, approvedByUserId = approvedByUserId,
    createdByUserId = createdByUserId, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun ReturnInvoice.toEntity(): ReturnInvoiceEntity = ReturnInvoiceEntity(
    id = id, returnNumber = returnNumber, originalInvoiceId = originalInvoiceId,
    originalPoId = originalPoId, customerId = customerId, supplierId = supplierId,
    items = "", subtotal = subtotal, taxAmount = taxAmount,
    discount = discount, refundAmount = refundAmount, total = total,
    reason = reason, status = status, approvedByUserId = approvedByUserId,
    createdByUserId = createdByUserId, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun SupplierEntity.toDomain(): Supplier = Supplier(
    id = id, businessId = businessId, name = name, nameUr = nameUr,
    contactPerson = contactPerson, phone = phone, email = email,
    address = address, paymentTerms = paymentTerms, creditLimit = creditLimit,
    currentBalance = currentBalance, taxNumber = taxNumber,
    bankAccount = bankAccount, bankName = bankName,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun Supplier.toEntity(): SupplierEntity = SupplierEntity(
    id = id, businessId = businessId, name = name, nameUr = nameUr,
    contactPerson = contactPerson, phone = phone, email = email,
    address = address, paymentTerms = paymentTerms, creditLimit = creditLimit,
    currentBalance = currentBalance, taxNumber = taxNumber,
    bankAccount = bankAccount, bankName = bankName,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun StockTransferEntity.toDomain(): StockTransfer = StockTransfer(
    id = id, transferNumber = transferNumber, fromLocationId = fromLocationId,
    toLocationId = toLocationId, productId = productId, quantity = quantity,
    status = status, requestedByUserId = requestedByUserId,
    approvedByUserId = approvedByUserId, createdAt = createdAt,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun StockTransfer.toEntity(): StockTransferEntity = StockTransferEntity(
    id = id, transferNumber = transferNumber, fromLocationId = fromLocationId,
    toLocationId = toLocationId, productId = productId, quantity = quantity,
    status = status, requestedByUserId = requestedByUserId,
    approvedByUserId = approvedByUserId, createdAt = createdAt,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun StockLedgerEntity.toDomain(): StockLedgerEntry = StockLedgerEntry(
    id = id, productId = productId, locationId = locationId,
    movementType = movementType, quantity = quantity,
    referenceType = referenceType, referenceId = referenceId, createdAt = createdAt
)

fun StockLedgerEntry.toEntity(): StockLedgerEntity = StockLedgerEntity(
    id = id, productId = productId, locationId = locationId,
    movementType = movementType, quantity = quantity,
    referenceType = referenceType, referenceId = referenceId, createdAt = createdAt
)

fun DocumentVaultEntity.toDomain(): DocumentVault = DocumentVault(
    id = id, businessId = businessId, documentType = documentType,
    fileName = fileName, filePath = filePath, thumbnailPath = thumbnailPath,
    ocrRawText = ocrRawText, extractionStatus = extractionStatus,
    linkedRecordType = linkedRecordType, linkedRecordId = linkedRecordId,
    scannedByUserId = scannedByUserId, scannedAt = scannedAt,
    locationId = locationId, confidence = confidence,
    uploadedAt = uploadedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun DocumentVault.toEntity(): DocumentVaultEntity = DocumentVaultEntity(
    id = id, businessId = businessId, documentType = documentType,
    fileName = fileName, filePath = filePath, thumbnailPath = thumbnailPath,
    ocrRawText = ocrRawText, extractionStatus = extractionStatus,
    linkedRecordType = linkedRecordType, linkedRecordId = linkedRecordId,
    scannedByUserId = scannedByUserId, scannedAt = scannedAt,
    locationId = locationId, confidence = confidence,
    uploadedAt = uploadedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun ApprovalRequestEntity.toDomain(): ApprovalRequest = ApprovalRequest(
    id = id, requestType = requestType, entityId = entityId,
    entityType = entityType, requestedByUserId = requestedByUserId,
    currentApproverUserId = currentApproverUserId, status = status,
    approvalChainJson = approvalChainJson, comments = comments,
    createdAt = createdAt, resolvedAt = resolvedAt, syncStatus = syncStatus
)

fun ApprovalRequest.toEntity(): ApprovalRequestEntity = ApprovalRequestEntity(
    id = id, requestType = requestType, entityId = entityId,
    entityType = entityType, requestedByUserId = requestedByUserId,
    currentApproverUserId = currentApproverUserId, status = status,
    approvalChainJson = approvalChainJson, comments = comments,
    createdAt = createdAt, resolvedAt = resolvedAt, syncStatus = syncStatus
)

fun AuditLogEntity.toDomain(): AuditLog = AuditLog(
    id = id, businessId = businessId, userId = userId,
    userName = userName, action = action, entityType = entityType,
    entityId = entityId, oldValueJson = oldValueJson,
    newValueJson = newValueJson, deviceId = deviceId,
    ipAddress = ipAddress, timestamp = timestamp
)

fun AuditLog.toEntity(): AuditLogEntity = AuditLogEntity(
    id = id, businessId = businessId, userId = userId,
    userName = userName, action = action, entityType = entityType,
    entityId = entityId, oldValueJson = oldValueJson,
    newValueJson = newValueJson, deviceId = deviceId,
    ipAddress = ipAddress, timestamp = timestamp
)