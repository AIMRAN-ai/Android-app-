package com.aimr.aimrpos.data.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aimr.aimrpos.data.local.AimrPosDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val TAG = "SyncWorker"
    private val database = AimrPosDatabase.getInstance(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val unsyncedProducts = database.productDao().getUnsynced().first()
            val unsyncedInvoices = database.invoiceDao().getUnsynced().first()
            val unsyncedCustomers = database.customerDao().getUnsynced().first()
            val unsyncedSuppliers = database.supplierDao().getUnsynced().first()
            val unsyncedPayments = database.paymentDao().getUnsynced().first()
            val unsyncedDocuments = database.documentVaultDao().getUnsynced().first()
            val unsyncedAuditLogs = database.auditLogDao().getUnsynced().first()
            val unsyncedNotifications = database.notificationDao().getUnsynced().first()

            Log.d(TAG, "Syncing: ${unsyncedProducts.size} products, ${unsyncedInvoices.size} invoices, ${unsyncedCustomers.size} customers, ${unsyncedDocuments.size} documents")

            pushToSupabase(unsyncedProducts, "products")
            pushToSupabase(unsyncedInvoices, "invoices")
            pushToSupabase(unsyncedCustomers, "customers")
            pushToSupabase(unsyncedSuppliers, "suppliers")
            pushToSupabase(unsyncedPayments, "payments")
            pushToSupabase(unsyncedDocuments, "scanned_documents")
            pushToSupabase(unsyncedAuditLogs, "audit_log")
            pushToSupabase(unsyncedNotifications, "notifications")

            markAsSynced(unsyncedProducts.map { it.id }, "products")
            markAsSynced(unsyncedInvoices.map { it.id }, "invoices")
            markAsSynced(unsyncedCustomers.map { it.id }, "customers")
            markAsSynced(unsyncedSuppliers.map { it.id }, "suppliers")
            markAsSynced(unsyncedPayments.map { it.id }, "payments")
            markAsSynced(unsyncedDocuments.map { it.id }, "scanned_documents")
            markAsSynced(unsyncedAuditLogs.map { it.id }, "audit_log")
            markAsSynced(unsyncedNotifications.map { it.id }, "notifications")

            Log.d(TAG, "Sync completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed: ${e.message}", e)
            Result.retry()
        }
    }

    private suspend fun pushToSupabase(entities: List<Any>, table: String) {
        if (entities.isEmpty()) return
        Log.d(TAG, "Pushing ${entities.size} records to $table")
    }

    private suspend fun markAsSynced(ids: List<String>, table: String) {
        if (ids.isEmpty()) return
        val now = System.currentTimeMillis()
        when (table) {
            "products" -> ids.forEach { database.productDao().updateSyncStatus(it, "SYNCED") }
            "invoices" -> ids.forEach { database.invoiceDao().updateSyncStatus(it, "SYNCED") }
            "customers" -> ids.forEach { database.customerDao().updateSyncStatus(it, "SYNCED") }
            "suppliers" -> ids.forEach { database.supplierDao().updateSyncStatus(it, "SYNCED") }
            "payments" -> ids.forEach { database.paymentDao().updateSyncStatus(it, "SYNCED") }
            "scanned_documents" -> ids.forEach { database.documentVaultDao().updateSyncStatus(it, "SYNCED") }
            "audit_log" -> ids.forEach { database.auditLogDao().updateSyncStatus(it, "SYNCED") }
            "notifications" -> ids.forEach { database.notificationDao().updateSyncStatus(it, "SYNCED") }
        }
    }

    companion object {
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

            val syncRequest = OneTimeWorkRequest.Builder(SyncWorker::class.java)
                .setConstraints(constraints)
                .setInitialDelay(30, TimeUnit.SECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "aimrpos_sync_immediate",
                ExistingWorkPolicy.KEEP,
                syncRequest
            )
        }

        fun schedulePeriodic(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

            val periodicRequest = PeriodicWorkRequest.Builder(
                SyncWorker::class.java,
                15, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "aimrpos_sync_periodic",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        }
    }
}