package com.aimr.aimrpos.data.repository

import com.aimr.aimrpos.data.local.dao.BusinessDao
import com.aimr.aimrpos.data.local.dao.CategoryDao
import com.aimr.aimrpos.data.local.dao.CustomerDao
import com.aimr.aimrpos.data.local.dao.InvoiceDao
import com.aimr.aimrpos.data.local.dao.InvoiceItemDao
import com.aimr.aimrpos.data.local.dao.ProductDao
import com.aimr.aimrpos.data.local.dao.UserDao
import com.aimr.aimrpos.data.local.entity.BusinessEntity
import com.aimr.aimrpos.data.local.entity.CategoryEntity
import com.aimr.aimrpos.data.local.entity.CustomerEntity
import com.aimr.aimrpos.data.local.entity.InvoiceEntity
import com.aimr.aimrpos.data.local.entity.InvoiceItemEntity
import com.aimr.aimrpos.data.local.entity.ProductEntity
import com.aimr.aimrpos.data.local.entity.UserEntity
import com.aimr.aimrpos.domain.model.Business
import com.aimr.aimrpos.domain.model.Category
import com.aimr.aimrpos.domain.model.Customer
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.InvoiceItem
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.model.User
import com.aimr.aimrpos.domain.repository.BusinessRepository
import com.aimr.aimrpos.domain.repository.CategoryRepository
import com.aimr.aimrpos.domain.repository.CustomerRepository
import com.aimr.aimrpos.domain.repository.InvoiceItemRepository
import com.aimr.aimrpos.domain.repository.InvoiceRepository
import com.aimr.aimrpos.domain.repository.ProductRepository
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

fun ProductEntity.toDomain(): Product = Product(
    id = id, name = name, nameUr = nameUr, sku = sku, barcode = barcode,
    categoryId = categoryId, costPrice = costPrice, salePrice = salePrice,
    stockQty = stockQty, unit = unit, lowStockThreshold = lowStockThreshold,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id, name = name, nameUr = nameUr, sku = sku, barcode = barcode,
    categoryId = categoryId, costPrice = costPrice, salePrice = salePrice,
    stockQty = stockQty, unit = unit, lowStockThreshold = lowStockThreshold,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun InvoiceEntity.toDomain(): Invoice = Invoice(
    id = id, invoiceNumber = invoiceNumber, customerId = customerId,
    subtotal = subtotal, taxAmount = taxAmount, discount = discount,
    total = total, paymentStatus = paymentStatus, paymentMethod = paymentMethod,
    createdByUserId = createdByUserId, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun Invoice.toEntity(): InvoiceEntity = InvoiceEntity(
    id = id, invoiceNumber = invoiceNumber, customerId = customerId,
    subtotal = subtotal, taxAmount = taxAmount, discount = discount,
    total = total, paymentStatus = paymentStatus, paymentMethod = paymentMethod,
    createdByUserId = createdByUserId, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun InvoiceItemEntity.toDomain(): InvoiceItem = InvoiceItem(
    id = id, invoiceId = invoiceId, productId = productId,
    quantity = quantity, unitPrice = unitPrice, taxRate = taxRate,
    lineTotal = lineTotal
)

fun InvoiceItem.toEntity(): InvoiceItemEntity = InvoiceItemEntity(
    id = id, invoiceId = invoiceId, productId = productId,
    quantity = quantity, unitPrice = unitPrice, taxRate = taxRate,
    lineTotal = lineTotal
)

fun CustomerEntity.toDomain(): Customer = Customer(
    id = id, name = name, phone = phone, address = address,
    creditBalance = creditBalance, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun Customer.toEntity(): CustomerEntity = CustomerEntity(
    id = id, name = name, phone = phone, address = address,
    creditBalance = creditBalance, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun UserEntity.toDomain(): User = User(
    id = id, businessId = businessId, name = name, role = role,
    phone = phone, pinHash = pinHash, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id, businessId = businessId, name = name, role = role,
    phone = phone, pinHash = pinHash, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun BusinessEntity.toDomain(): Business = Business(
    id = id, name = name, address = address, taxNumber = taxNumber,
    currency = currency, subscriptionTier = subscriptionTier,
    subscriptionExpiry = subscriptionExpiry, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun Business.toEntity(): BusinessEntity = BusinessEntity(
    id = id, name = name, address = address, taxNumber = taxNumber,
    currency = currency, subscriptionTier = subscriptionTier,
    subscriptionExpiry = subscriptionExpiry, updatedAt = updatedAt,
    isDeleted = isDeleted, syncStatus = syncStatus
)

fun CategoryEntity.toDomain(): Category = Category(
    id = id, name = name, nameUr = nameUr,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id, name = name, nameUr = nameUr,
    updatedAt = updatedAt, isDeleted = isDeleted, syncStatus = syncStatus
)