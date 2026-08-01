package com.aimr.aimrpos.domain.repository

import com.aimr.aimrpos.domain.model.Business
import com.aimr.aimrpos.domain.model.Category
import com.aimr.aimrpos.domain.model.Customer
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.InvoiceItem
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun upsert(product: Product)
    suspend fun getById(id: String): Product?
    fun getAll(): Flow<List<Product>>
    fun search(query: String): Flow<List<Product>>
    fun getByCategory(categoryId: String): Flow<List<Product>>
    fun getLowStock(): Flow<List<Product>>
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