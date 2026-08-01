package com.aimr.aimrpos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
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
import com.aimr.aimrpos.data.local.dao.UserDao
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
        LocationEntity::class,
        ApprovalRequestEntity::class,
        SupplierEntity::class,
        StockTransferEntity::class,
        StockLedgerEntity::class,
        PaymentEntity::class
    ],
    version = 3,
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
    abstract fun locationDao(): LocationDao
    abstract fun approvalRequestDao(): ApprovalRequestDao
    abstract fun supplierDao(): SupplierDao
    abstract fun stockTransferDao(): StockTransferDao
    abstract fun stockLedgerDao(): StockLedgerDao
    abstract fun paymentDao(): PaymentDao
}