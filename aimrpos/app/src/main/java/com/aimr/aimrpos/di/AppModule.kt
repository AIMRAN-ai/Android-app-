package com.aimr.aimrpos.di

import android.content.Context
import androidx.room.Room
import com.aimr.aimrpos.data.local.AimrPosDatabase
import com.aimr.aimrpos.data.local.dao.ApprovalRequestDao
import com.aimr.aimrpos.data.local.dao.AuditLogDao
import com.aimr.aimrpos.data.local.dao.BusinessDao
import com.aimr.aimrpos.data.local.dao.CategoryDao
import com.aimr.aimrpos.data.local.dao.CustomerDao
import com.aimr.aimrpos.data.local.dao.DashboardWidgetDao
import com.aimr.aimrpos.data.local.dao.DocumentVaultDao
import com.aimr.aimrpos.data.local.dao.GRNDao
import com.aimr.aimrpos.data.local.dao.InvoiceDao
import com.aimr.aimrpos.data.local.dao.InvoiceItemDao
import com.aimr.aimrpos.data.local.dao.LocationDao
import com.aimr.aimrpos.data.local.dao.PaymentDao
import com.aimr.aimrpos.data.local.dao.PriceHistoryDao
import com.aimr.aimrpos.data.local.dao.PriceTierDao
import com.aimr.aimrpos.data.local.dao.ProductBatchDao
import com.aimr.aimrpos.data.local.dao.ProductDao
import com.aimr.aimrpos.data.local.dao.PromotionDao
import com.aimr.aimrpos.data.local.dao.PurchaseOrderDao
import com.aimr.aimrpos.data.local.dao.PurchaseOrderItemDao
import com.aimr.aimrpos.data.local.dao.ReturnInvoiceDao
import com.aimr.aimrpos.data.local.dao.ScaleItemDao
import com.aimr.aimrpos.data.local.dao.StockLedgerDao
import com.aimr.aimrpos.data.local.dao.StockTransferDao
import com.aimr.aimrpos.data.local.dao.SupplierDao
import com.aimr.aimrpos.data.local.dao.UserDao
import com.aimr.aimrpos.data.local.dao.LoyaltyCustomerDao
import com.aimr.aimrpos.data.local.dao.LoyaltyRuleDao
import com.aimr.aimrpos.data.local.dao.LoyaltyTransactionDao
import com.aimr.aimrpos.data.repository.ProductRepositoryImpl
import com.aimr.aimrpos.data.repository.InvoiceRepositoryImpl
import com.aimr.aimrpos.data.repository.InvoiceItemRepositoryImpl
import com.aimr.aimrpos.data.repository.CustomerRepositoryImpl
import com.aimr.aimrpos.data.repository.UserRepositoryImpl
import com.aimr.aimrpos.data.repository.BusinessRepositoryImpl
import com.aimr.aimrpos.data.repository.CategoryRepositoryImpl
import com.aimr.aimrpos.data.repository.DashboardWidgetRepositoryImpl
import com.aimr.aimrpos.data.repository.PriceHistoryRepositoryImpl
import com.aimr.aimrpos.data.repository.PriceTierRepositoryImpl
import com.aimr.aimrpos.data.repository.PromotionRepositoryImpl
import com.aimr.aimrpos.data.repository.ScaleItemRepositoryImpl
import com.aimr.aimrpos.data.repository.ProductBatchRepositoryImpl
import com.aimr.aimrpos.data.repository.LoyaltyCustomerRepositoryImpl
import com.aimr.aimrpos.data.repository.LoyaltyTransactionRepositoryImpl
import com.aimr.aimrpos.data.repository.LoyaltyRuleRepositoryImpl
import com.aimr.aimrpos.data.repository.ScanSessionRepositoryImpl
import com.aimr.aimrpos.domain.repository.CategoryRepository
import com.aimr.aimrpos.domain.repository.DashboardWidgetRepository
import com.aimr.aimrpos.domain.repository.ScanSessionRepository
import com.aimr.aimrpos.domain.repository.PriceTierRepository
import com.aimr.aimrpos.domain.repository.PriceHistoryRepository
import com.aimr.aimrpos.domain.repository.PromotionRepository
import com.aimr.aimrpos.domain.repository.ScaleItemRepository
import com.aimr.aimrpos.domain.repository.ProductBatchRepository
import com.aimr.aimrpos.domain.repository.LoyaltyCustomerRepository
import com.aimr.aimrpos.domain.repository.LoyaltyTransactionRepository
import com.aimr.aimrpos.domain.repository.LoyaltyRuleRepository
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
        )
            .fallbackToDestructiveMigration()
            .build()
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
    fun provideScanSessionDao(database: AimrPosDatabase): ScanSessionDao = database.scanSessionDao()

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
    fun provideRoleDao(database: AimrPosDatabase): RoleDao = database.roleDao()

    @Provides
    fun providePermissionDao(database: AimrPosDatabase): PermissionDao = database.permissionDao()

    @Provides
    fun provideUserRoleDao(database: AimrPosDatabase): UserRoleDao = database.userRoleDao()

    @Provides
    fun provideCurrencyDao(database: AimrPosDatabase): CurrencyDao = database.currencyDao()

    @Provides
    fun provideExchangeRateDao(database: AimrPosDatabase): ExchangeRateDao = database.exchangeRateDao()

    @Provides
    fun provideWorkflowRuleDao(database: AimrPosDatabase): WorkflowRuleDao = database.workflowRuleDao()

    @Provides
    fun provideSalesForecastDao(database: AimrPosDatabase): SalesForecastDao = database.salesForecastDao()

    @Provides
    fun provideAnalyticsSnapshotDao(database: AimrPosDatabase): AnalyticsSnapshotDao = database.analyticsSnapshotDao()

    @Provides
    fun provideNotificationDao(database: AimrPosDatabase): NotificationDao = database.notificationDao()

    @Provides
    fun provideBusinessUnitDao(database: AimrPosDatabase): BusinessUnitDao = database.businessUnitDao()

    @Provides
    fun provideDashboardWidgetDao(database: AimrPosDatabase): DashboardWidgetDao = database.dashboardWidgetDao()

    @Provides
    fun providePriceTierDao(database: AimrPosDatabase): PriceTierDao = database.priceTierDao()

    @Provides
    fun providePriceHistoryDao(database: AimrPosDatabase): PriceHistoryDao = database.priceHistoryDao()

    @Provides
    fun providePromotionDao(database: AimrPosDatabase): PromotionDao = database.promotionDao()

    @Provides
    fun provideScaleItemDao(database: AimrPosDatabase): ScaleItemDao = database.scaleItemDao()

    @Provides
    fun provideProductBatchDao(database: AimrPosDatabase): ProductBatchDao = database.productBatchDao()

    @Provides
    fun provideLoyaltyCustomerDao(database: AimrPosDatabase): LoyaltyCustomerDao = database.loyaltyCustomerDao()

    @Provides
    fun provideLoyaltyTransactionDao(database: AimrPosDatabase): LoyaltyTransactionDao = database.loyaltyTransactionDao()

    @Provides
    fun provideLoyaltyRuleDao(database: AimrPosDatabase): LoyaltyRuleDao = database.loyaltyRuleDao()

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

    @Provides
    @Singleton
    fun provideRoleRepository(dao: RoleDao): RoleRepository = RoleRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePermissionRepository(dao: PermissionDao): PermissionRepository = PermissionRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUserRoleRepository(dao: UserRoleDao): UserRoleRepository = UserRoleRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCurrencyRepository(dao: CurrencyDao): CurrencyRepository = CurrencyRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideExchangeRateRepository(dao: ExchangeRateDao): ExchangeRateRepository = ExchangeRateRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideWorkflowRuleRepository(dao: WorkflowRuleDao): WorkflowRuleRepository = WorkflowRuleRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideSalesForecastRepository(dao: SalesForecastDao): SalesForecastRepository = SalesForecastRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAnalyticsSnapshotRepository(dao: AnalyticsSnapshotDao): AnalyticsSnapshotRepository = AnalyticsSnapshotRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideNotificationRepository(dao: NotificationDao): NotificationRepository = NotificationRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideBusinessUnitRepository(dao: BusinessUnitDao): BusinessUnitRepository = BusinessUnitRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideScanSessionRepository(dao: ScanSessionDao): ScanSessionRepository = ScanSessionRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideDashboardWidgetRepository(dao: DashboardWidgetDao): DashboardWidgetRepository = DashboardWidgetRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePriceTierRepository(dao: PriceTierDao): PriceTierRepository = PriceTierRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePriceHistoryRepository(dao: PriceHistoryDao): PriceHistoryRepository = PriceHistoryRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePromotionRepository(dao: PromotionDao): PromotionRepository = PromotionRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideScaleItemRepository(dao: ScaleItemDao): ScaleItemRepository = ScaleItemRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideProductBatchRepository(dao: ProductBatchDao): ProductBatchRepository = ProductBatchRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideLoyaltyCustomerRepository(dao: LoyaltyCustomerDao): LoyaltyCustomerRepository = LoyaltyCustomerRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideLoyaltyTransactionRepository(dao: LoyaltyTransactionDao): LoyaltyTransactionRepository = LoyaltyTransactionRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideLoyaltyRuleRepository(dao: LoyaltyRuleDao): LoyaltyRuleRepository = LoyaltyRuleRepositoryImpl(dao)
}
