package com.aimr.aimrpos.di

import android.content.Context
import androidx.room.Room
import com.aimr.aimrpos.data.local.AimrPosDatabase
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
import com.aimr.aimrpos.data.repository.ProductRepositoryImpl
import com.aimr.aimrpos.data.repository.InvoiceRepositoryImpl
import com.aimr.aimrpos.data.repository.InvoiceItemRepositoryImpl
import com.aimr.aimrpos.data.repository.CustomerRepositoryImpl
import com.aimr.aimrpos.data.repository.UserRepositoryImpl
import com.aimr.aimrpos.data.repository.BusinessRepositoryImpl
import com.aimr.aimrpos.data.repository.CategoryRepositoryImpl
import com.aimr.aimrpos.domain.repository.ProductRepository
import com.aimr.aimrpos.domain.repository.InvoiceRepository
import com.aimr.aimrpos.domain.repository.InvoiceItemRepository
import com.aimr.aimrpos.domain.repository.CustomerRepository
import com.aimr.aimrpos.domain.repository.UserRepository
import com.aimr.aimrpos.domain.repository.BusinessRepository
import com.aimr.aimrpos.domain.repository.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AimrPosDatabase {
        return Room.databaseBuilder(
            context,
            AimrPosDatabase::class.java,
            "aimr_pos.db"
        ).build()
    }

    @Provides
    fun provideLocationDao(database: AimrPosDatabase): LocationDao = database.locationDao()

    @Provides
    fun providePurchaseOrderDao(database: AimrPosDatabase): PurchaseOrderDao = database.purchaseOrderDao()

    @Provides
    fun providePurchaseOrderItemDao(database: AimrPosDatabase): PurchaseOrderItemDao = database.purchaseOrderItemDao()

    @Provides
    fun provideGrnDao(database: AimrPosDatabase): GRNDao = database.grnDao()

    @Provides
    fun provideReturnInvoiceDao(database: AimrPosDatabase): ReturnInvoiceDao = database.returnInvoiceDao()

    @Provides
    fun provideAuditLogDao(database: AimrPosDatabase): AuditLogDao = database.auditLogDao()

    @Provides
    fun provideDocumentVaultDao(database: AimrPosDatabase): DocumentVaultDao = database.documentVaultDao()

    @Provides
    fun provideApprovalRequestDao(database: AimrPosDatabase): ApprovalRequestDao = database.approvalRequestDao()

    @Provides
    fun provideSupplierDao(database: AimrPosDatabase): SupplierDao = database.supplierDao()

    @Provides
    fun provideStockTransferDao(database: AimrPosDatabase): StockTransferDao = database.stockTransferDao()

    @Provides
    fun provideStockLedgerDao(database: AimrPosDatabase): StockLedgerDao = database.stockLedgerDao()

    @Provides
    fun providePaymentDao(database: AimrPosDatabase): PaymentDao = database.paymentDao()

    @Provides
    @Singleton
    fun provideProductRepository(dao: ProductDao): ProductRepository = ProductRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideInvoiceRepository(dao: InvoiceDao): InvoiceRepository = InvoiceRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideInvoiceItemRepository(dao: InvoiceItemDao): InvoiceItemRepository = InvoiceItemRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCustomerRepository(dao: CustomerDao): CustomerRepository = CustomerRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository = UserRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideBusinessRepository(dao: BusinessDao): BusinessRepository = BusinessRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCategoryRepository(dao: CategoryDao): CategoryRepository = CategoryRepositoryImpl(dao)
}