package com.aimr.aimrpos.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
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
import com.aimr.aimrpos.data.local.dao.RoleDao
import com.aimr.aimrpos.data.local.dao.ScaleItemDao
import com.aimr.aimrpos.data.local.dao.ScanSessionDao
import com.aimr.aimrpos.data.local.dao.StockLedgerDao
import com.aimr.aimrpos.data.local.dao.StockTransferDao
import com.aimr.aimrpos.data.local.dao.SupplierDao
import com.aimr.aimrpos.data.local.dao.UserDao
import com.aimr.aimrpos.data.local.dao.UserRoleDao
import com.aimr.aimrpos.data.local.dao.CurrencyDao
import com.aimr.aimrpos.data.local.dao.ExchangeRateDao
import com.aimr.aimrpos.data.local.dao.WorkflowRuleDao
import com.aimr.aimrpos.data.local.dao.SalesForecastDao
import com.aimr.aimrpos.data.local.dao.AnalyticsSnapshotDao
import com.aimr.aimrpos.data.local.dao.NotificationDao
import com.aimr.aimrpos.data.local.dao.BusinessUnitDao
import com.aimr.aimrpos.data.local.dao.LoyaltyCustomerDao
import com.aimr.aimrpos.data.local.dao.LoyaltyRuleDao
import com.aimr.aimrpos.data.local.dao.LoyaltyTransactionDao
import com.aimr.aimrpos.data.local.dao.PermissionDao
import com.aimr.aimrpos.data.local.dao.StockTransferDao
import com.aimr.aimrpos.data.local.dao.SupplierDao
import com.aimr.aimrpos.data.local.dao.UserDao
import com.aimr.aimrpos.data.local.dao.LoyaltyCustomerDao
import com.aimr.aimrpos.data.local.dao.LoyaltyRuleDao
import com.aimr.aimrpos.data.local.dao.LoyaltyTransactionDao
import com.aimr.aimrpos.data.local.entity.RoleEntity
import com.aimr.aimrpos.data.local.entity.PermissionEntity
import com.aimr.aimrpos.data.local.entity.UserRoleEntity
import com.aimr.aimrpos.data.local.entity.CurrencyEntity
import com.aimr.aimrpos.data.local.entity.WorkflowRuleEntity
import com.aimr.aimrpos.data.local.entity.SalesForecastEntity
import com.aimr.aimrpos.data.local.entity.AnalyticsSnapshotEntity
import com.aimr.aimrpos.data.local.entity.NotificationEntity
import com.aimr.aimrpos.data.local.entity.ExchangeRateEntity
import com.aimr.aimrpos.data.local.entity.BusinessUnitEntity
import com.aimr.aimrpos.data.local.entity.ScanSessionEntity
import com.aimr.aimrpos.data.local.entity.DashboardWidgetEntity
import com.aimr.aimrpos.data.local.entity.PriceTierEntity
import com.aimr.aimrpos.data.local.entity.PriceHistoryEntity
import com.aimr.aimrpos.data.local.entity.PromotionEntity
import com.aimr.aimrpos.data.local.entity.ScaleItemEntity
import com.aimr.aimrpos.data.local.entity.ProductBatchEntity
import com.aimr.aimrpos.data.local.entity.LoyaltyCustomerEntity
import com.aimr.aimrpos.data.local.entity.LoyaltyTransactionEntity
import com.aimr.aimrpos.data.local.entity.LoyaltyRuleEntity
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
import com.aimr.aimrpos.data.local.entity.UserEntity

@Database(
    entities = [
        ProductEntity::class,
        InvoiceEntity::class,
        InvoiceItemEntity::class,
        CustomerEntity::class,
        UserEntity::class,
        BusinessEntity::class,
        CategoryEntity::class,
        PurchaseOrderEntity::class,
        PurchaseOrderItemEntity::class,
        GRNEntity::class,
        ReturnInvoiceEntity::class,
        AuditLogEntity::class,
        DocumentVaultEntity::class,
        ScanSessionEntity::class,
        LocationEntity::class,
        ApprovalRequestEntity::class,
        SupplierEntity::class,
        StockTransferEntity::class,
        StockLedgerEntity::class,
        PaymentEntity::class,
        RoleEntity::class,
        PermissionEntity::class,
        UserRoleEntity::class,
        CurrencyEntity::class,
        WorkflowRuleEntity::class,
        SalesForecastEntity::class,
        AnalyticsSnapshotEntity::class,
        NotificationEntity::class,
        ExchangeRateEntity::class,
        BusinessUnitEntity::class,
        DashboardWidgetEntity::class,
        PriceTierEntity::class,
        PriceHistoryEntity::class,
        PromotionEntity::class,
        ScaleItemEntity::class,
        ProductBatchEntity::class,
        LoyaltyCustomerEntity::class,
        LoyaltyTransactionEntity::class,
        LoyaltyRuleEntity::class
    ],
    version = 11,
    exportSchema = false
)
abstract class AimrPosDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun invoiceItemDao(): InvoiceItemDao
    abstract fun customerDao(): CustomerDao
    abstract fun userDao(): UserDao
    abstract fun businessDao(): BusinessDao
    abstract fun categoryDao(): CategoryDao
    abstract fun purchaseOrderDao(): PurchaseOrderDao
    abstract fun purchaseOrderItemDao(): PurchaseOrderItemDao
    abstract fun grnDao(): GRNDao
    abstract fun returnInvoiceDao(): ReturnInvoiceDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun documentVaultDao(): DocumentVaultDao
    abstract fun scanSessionDao(): ScanSessionDao
    abstract fun locationDao(): LocationDao
    abstract fun approvalRequestDao(): ApprovalRequestDao
    abstract fun supplierDao(): SupplierDao
    abstract fun stockTransferDao(): StockTransferDao
    abstract fun stockLedgerDao(): StockLedgerDao
    abstract fun paymentDao(): PaymentDao
    abstract fun roleDao(): RoleDao
    abstract fun permissionDao(): PermissionDao
    abstract fun userRoleDao(): UserRoleDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun workflowRuleDao(): WorkflowRuleDao
    abstract fun salesForecastDao(): SalesForecastDao
    abstract fun analyticsSnapshotDao(): AnalyticsSnapshotDao
    abstract fun notificationDao(): NotificationDao

    abstract fun businessUnitDao(): BusinessUnitDao

    abstract fun dashboardWidgetDao(): DashboardWidgetDao

    abstract fun priceTierDao(): PriceTierDao

    abstract fun priceHistoryDao(): PriceHistoryDao

    abstract fun promotionDao(): PromotionDao

    abstract fun scaleItemDao(): ScaleItemDao

    abstract fun productBatchDao(): ProductBatchDao

    abstract fun loyaltyCustomerDao(): LoyaltyCustomerDao

    abstract fun loyaltyTransactionDao(): LoyaltyTransactionDao

    abstract fun loyaltyRuleDao(): LoyaltyRuleDao

    companion object {
        @Volatile
        private var INSTANCE: AimrPosDatabase? = null

        fun getInstance(context: Context): AimrPosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AimrPosDatabase::class.java,
                    "aimr_pos.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}