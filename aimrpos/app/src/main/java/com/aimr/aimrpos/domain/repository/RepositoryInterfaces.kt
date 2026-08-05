package com.aimr.aimrpos.domain.repository

import com.aimr.aimrpos.domain.model.Business
import com.aimr.aimrpos.domain.model.Category
import com.aimr.aimrpos.domain.model.Customer
import com.aimr.aimrpos.domain.model.DocumentVault
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
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun upsert(product: Product)
    suspend fun getById(id: String): Product?
    fun getAll(): Flow<List<Product>>
    fun search(query: String): Flow<List<Product>>
    fun getByCategory(categoryId: String): Flow<List<Product>>
    fun getLowStock(): Flow<List<Product>>
    suspend fun getByBarcode(barcode: String): Product?
    suspend fun getByQrCode(qrCode: String): Product?
    fun getByProductType(type: String): Flow<List<Product>>
    suspend fun deductStock(productId: String, quantity: Double): Boolean
    suspend fun addStock(productId: String, quantity: Double)
    suspend fun updateSyncStatus(id: String, status: String)
}

interface InvoiceRepository {
    suspend fun upsert(invoice: Invoice)
    suspend fun getById(id: String): Invoice?
    fun getAll(): Flow<List<Invoice>>
    fun getToday(startOfDay: Long): Flow<List<Invoice>>
    fun getByCustomer(customerId: String): Flow<List<Invoice>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface InvoiceItemRepository {
    suspend fun insert(item: InvoiceItem)
    fun getByInvoice(invoiceId: String): Flow<List<InvoiceItem>>
    suspend fun deleteByInvoice(invoiceId: String)
}

interface CustomerRepository {
    suspend fun upsert(customer: Customer)
    suspend fun getById(id: String): Customer?
    fun getAll(): Flow<List<Customer>>
    fun getWithCredit(): Flow<List<Customer>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface UserRepository {
    suspend fun upsert(user: User)
    suspend fun getById(id: String): User?
    fun getByBusiness(businessId: String): Flow<List<User>>
    fun getActive(id: String): Flow<User?>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface BusinessRepository {
    suspend fun upsert(business: Business)
    fun getActive(): Flow<Business?>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface CategoryRepository {
    suspend fun upsert(category: Category)
    fun getAll(): Flow<List<Category>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface LocationRepository {
    suspend fun upsert(location: Location)
    suspend fun getById(id: String): Location?
    fun getByBusiness(businessId: String): Flow<List<Location>>
    fun getAll(): Flow<List<Location>>
    fun getWarehouses(): Flow<List<Location>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface PurchaseOrderRepository {
    suspend fun upsert(purchaseOrder: PurchaseOrder)
    suspend fun getById(id: String): PurchaseOrder?
    fun getAll(): Flow<List<PurchaseOrder>>
    fun getByStatus(status: String): Flow<List<PurchaseOrder>>
    fun getBySupplier(supplierId: String): Flow<List<PurchaseOrder>>
    fun getByLocation(locationId: String): Flow<List<PurchaseOrder>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface GRNRepository {
    suspend fun upsert(grn: com.aimr.aimrpos.domain.model.GRN)
    suspend fun getById(id: String): com.aimr.aimrpos.domain.model.GRN?
    fun getAll(): Flow<List<com.aimr.aimrpos.domain.model.GRN>>
    fun getByStatus(status: String): Flow<List<com.aimr.aimrpos.domain.model.GRN>>
    fun getByPurchaseOrder(poId: String): Flow<List<com.aimr.aimrpos.domain.model.GRN>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface ReturnInvoiceRepository {
    suspend fun upsert(returnInvoice: ReturnInvoice)
    suspend fun getById(id: String): ReturnInvoice?
    fun getAll(): Flow<List<ReturnInvoice>>
    fun getByOriginalInvoice(invoiceId: String): Flow<List<ReturnInvoice>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface SupplierRepository {
    suspend fun upsert(supplier: Supplier)
    suspend fun getById(id: String): Supplier?
    fun getAll(): Flow<List<Supplier>>
    fun getWithBalance(): Flow<List<Supplier>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface StockTransferRepository {
    suspend fun upsert(transfer: StockTransfer)
    suspend fun getById(id: String): StockTransfer?
    fun getAll(): Flow<List<StockTransfer>>
    fun getByStatus(status: String): Flow<List<StockTransfer>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface StockLedgerRepository {
    suspend fun insert(entry: StockLedgerEntry)
    fun getByProductAndLocation(productId: String, locationId: String): Flow<List<StockLedgerEntry>>
    fun getByLocation(locationId: String): Flow<List<StockLedgerEntry>>
    fun getByProduct(productId: String): Flow<List<StockLedgerEntry>>
}

interface DocumentVaultRepository {
    suspend fun upsert(doc: DocumentVault)
    suspend fun getById(id: String): DocumentVault?
    fun getAll(): Flow<List<DocumentVault>>
    fun getByEntity(entityId: String): Flow<List<DocumentVault>>
    fun getByType(type: String): Flow<List<DocumentVault>>
    fun getByLocation(locationId: String): Flow<List<DocumentVault>>
    fun getByExtractionStatus(status: String): Flow<List<DocumentVault>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface ApprovalRequestRepository {
    suspend fun upsert(request: com.aimr.aimrpos.domain.model.ApprovalRequest)
    suspend fun getById(id: String): com.aimr.aimrpos.domain.model.ApprovalRequest?
    fun getPending(): Flow<List<com.aimr.aimrpos.domain.model.ApprovalRequest>>
    fun getPendingForApprover(userId: String): Flow<List<com.aimr.aimrpos.domain.model.ApprovalRequest>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface AuditLogRepository {
    suspend fun insert(log: com.aimr.aimrpos.domain.model.AuditLog)
    fun getRecent(limit: Int): Flow<List<com.aimr.aimrpos.domain.model.AuditLog>>
    fun getByEntity(entityId: String): Flow<List<com.aimr.aimrpos.domain.model.AuditLog>>
    fun getByUser(userId: String): Flow<List<com.aimr.aimrpos.domain.model.AuditLog>>
    fun getByBusiness(businessId: String): Flow<List<com.aimr.aimrpos.domain.model.AuditLog>>
    fun getByDateRange(start: Long, end: Long): Flow<List<com.aimr.aimrpos.domain.model.AuditLog>>
}

interface PaymentRepository {
    suspend fun upsert(payment: Payment)
    suspend fun getById(id: String): Payment?
    fun getAll(): Flow<List<Payment>>
    fun getByInvoice(invoiceId: String): Flow<List<Payment>>
    fun getByCustomer(customerId: String): Flow<List<Payment>>
    fun getBySupplier(supplierId: String): Flow<List<Payment>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface RoleRepository {
    suspend fun upsert(role: com.aimr.aimrpos.domain.model.Role)
    suspend fun getById(id: String): com.aimr.aimrpos.domain.model.Role?
    fun getAll(): Flow<List<com.aimr.aimrpos.domain.model.Role>>
    fun getSystemRoles(): Flow<List<com.aimr.aimrpos.domain.model.Role>>
    suspend fun updateSyncStatus(id: String, status: String)
}

interface PermissionRepository {
    suspend fun insert(permission: com.aimr.aimrpos.domain.model.Permission)
    fun getAll(): Flow<List<com.aimr.aimrpos.domain.model.Permission>>
    fun getByCategory(category: String): Flow<List<com.aimr.aimrpos.domain.model.Permission>>
    suspend fun getByKey(key: String): com.aimr.aimrpos.domain.model.Permission?
}

interface UserRoleRepository {
    suspend fun assign(userId: String, roleId: String, assignedBy: String)
    fun getByUser(userId: String): Flow<List<com.aimr.aimrpos.domain.model.UserRole>>
    fun getByRole(roleId: String): Flow<List<com.aimr.aimrpos.domain.model.UserRole>>
    suspend fun revoke(userId: String, roleId: String)
    suspend fun updateSyncStatus(id: String, status: String)
}

interface CurrencyRepository {
    suspend fun upsert(currency: com.aimr.aimrpos.domain.model.Currency)
    fun getAll(): Flow<List<com.aimr.aimrpos.domain.model.Currency>>
    suspend fun getByCode(code: String): com.aimr.aimrpos.domain.model.Currency?
    suspend fun getBaseCurrency(): com.aimr.aimrpos.domain.model.Currency?
    suspend fun updateRate(code: String, rate: Double)
    suspend fun updateSyncStatus(code: String, status: String)
}

interface ExchangeRateRepository {
    suspend fun saveRate(rate: com.aimr.aimrpos.domain.model.ExchangeRate)
    suspend fun getLatest(from: String, to: String): com.aimr.aimrpos.domain.model.ExchangeRate?
    suspend fun getAtDate(from: String, to: String, date: Long): com.aimr.aimrpos.domain.model.ExchangeRate?
    fun getAllActive(): Flow<List<com.aimr.aimrpos.domain.model.ExchangeRate>>
}

interface WorkflowRuleRepository {
    suspend fun upsert(rule: com.aimr.aimrpos.domain.model.WorkflowRule)
    fun getActiveRules(): Flow<List<com.aimr.aimrpos.domain.model.WorkflowRule>>
    suspend fun getRule(entityType: String, action: String): com.aimr.aimrpos.domain.model.WorkflowRule?
    suspend fun setActive(id: String, isActive: Boolean)
    suspend fun updateSyncStatus(id: String, status: String)
}

interface SalesForecastRepository {
    suspend fun save(forecast: com.aimr.aimrpos.domain.model.SalesForecast)
    fun getBetween(from: Long, to: Long): Flow<List<com.aimr.aimrpos.domain.model.SalesForecast>>
    fun getForProduct(productId: String, limit: Int = 30): Flow<List<com.aimr.aimrpos.domain.model.SalesForecast>>
    fun getForCategory(categoryId: String, limit: Int = 30): Flow<List<com.aimr.aimrpos.domain.model.SalesForecast>>
}

interface AnalyticsSnapshotRepository {
    suspend fun save(snapshot: com.aimr.aimrpos.domain.model.AnalyticsSnapshot)
    fun getLatest(period: String = "DAILY", limit: Int = 30): Flow<List<com.aimr.aimrpos.domain.model.AnalyticsSnapshot>>
    fun getBetween(from: Long, to: Long): Flow<List<com.aimr.aimrpos.domain.model.AnalyticsSnapshot>>
    suspend fun getLatestSnapshot(): com.aimr.aimrpos.domain.model.AnalyticsSnapshot?
}

interface NotificationRepository {
    suspend fun insert(notification: com.aimr.aimrpos.domain.model.Notification)
    fun getByUser(userId: String): Flow<List<com.aimr.aimrpos.domain.model.Notification>>
    fun getUnreadByUser(userId: String): Flow<List<com.aimr.aimrpos.domain.model.Notification>>
    suspend fun markAsRead(id: String)
    suspend fun markAllAsRead(userId: String)
    suspend fun deleteById(id: String)
    suspend fun deleteOlderThan(userId: String, olderThan: Long)
}

interface BusinessUnitRepository {
    suspend fun insert(unit: com.aimr.aimrpos.domain.model.BusinessUnit)
    fun getActiveForBusiness(businessId: String): Flow<List<com.aimr.aimrpos.domain.model.BusinessUnit>>
    suspend fun getById(id: String): com.aimr.aimrpos.domain.model.BusinessUnit?
    suspend fun updateSyncStatus(id: String, status: String)
}

interface ScanSessionRepository {
    suspend fun upsert(session: com.aimr.aimrpos.domain.model.DocumentVault)
    suspend fun getById(id: String): com.aimr.aimrpos.domain.model.DocumentVault?
    fun getAll(): Flow<List<com.aimr.aimrpos.domain.model.DocumentVault>>
    fun getByType(type: String): Flow<List<com.aimr.aimrpos.domain.model.DocumentVault>>
    fun getByUser(userId: String): Flow<List<com.aimr.aimrpos.domain.model.DocumentVault>>
    fun getUnsynced(): Flow<List<com.aimr.aimrpos.domain.model.DocumentVault>>
    suspend fun updateOcrResult(id: String, ocrRawText: String, status: String, confidence: Float)
    suspend fun updateSyncStatus(id: String, status: String)
    suspend fun deleteById(id: String)
    suspend fun deleteSoftDeleted()
}

interface DashboardWidgetRepository {
    suspend fun upsert(widget: DashboardWidget)
    fun getAllVisible(): Flow<List<DashboardWidget>>
    suspend fun getById(id: String): DashboardWidget?
    fun getByType(type: String): Flow<List<DashboardWidget>>
    suspend fun updateLayout(id: String, x: Int, y: Int, width: Int, height: Int)
    suspend fun setVisibility(id: String, visible: Boolean)
    suspend fun updateConfig(id: String, config: String)
    suspend fun deleteById(id: String)
    suspend fun deleteSoftDeleted()
}