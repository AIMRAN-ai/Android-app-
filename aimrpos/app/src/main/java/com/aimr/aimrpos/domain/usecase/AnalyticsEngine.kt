package com.aimr.aimrpos.domain.usecase

import com.aimr.aimrpos.domain.model.AnalyticsSnapshot
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.repository.AnalyticsSnapshotRepository
import com.aimr.aimrpos.domain.repository.InvoiceRepository
import com.aimr.aimrpos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first

class AnalyticsEngine(
    private val invoiceRepository: InvoiceRepository,
    private val productRepository: ProductRepository,
    private val analyticsSnapshotRepository: AnalyticsSnapshotRepository
) {
    suspend fun generateDailySnapshot(): AnalyticsSnapshot {
        val startOfDay = getStartOfDay()
        val invoices = invoiceRepository.getToday(startOfDay).first()
        val products = productRepository.getAll().first()
        val totalSales = invoices.sumOf { it.total }
        val totalOrders = invoices.size
        val avgOrderValue = if (totalOrders > 0) totalSales / totalOrders else 0.0
        val lowStockCount = products.count { it.stockQty <= 5.0 && !it.isDeleted }
        val outstandingCredit = invoices.filter { it.paymentStatus == "PENDING" || it.paymentStatus == "CREDIT" }.sumOf { it.total }
        val stockValuation = products.filter { !it.isDeleted }.sumOf { it.stockQty * it.costPrice }

        val snapshot = AnalyticsSnapshot(
            snapshotDate = System.currentTimeMillis(),
            period = "DAILY",
            totalSales = totalSales,
            totalOrders = totalOrders,
            avgOrderValue = avgOrderValue,
            totalCustomers = 0,
            totalProducts = products.size,
            lowStockCount = lowStockCount,
            outstandingCredit = outstandingCredit,
            stockValuation = stockValuation
        )
        analyticsSnapshotRepository.save(snapshot)
        return snapshot
    }

    suspend fun generateWeeklySnapshot(): AnalyticsSnapshot {
        val startOfWeek = getStartOfWeek()
        val invoices = invoiceRepository.getAll().first().filter { it.updatedAt >= startOfWeek }
        val products = productRepository.getAll().first()
        val totalSales = invoices.sumOf { it.total }
        val totalOrders = invoices.size
        val avgOrderValue = if (totalOrders > 0) totalSales / totalOrders else 0.0
        val lowStockCount = products.count { it.stockQty <= 5.0 && !it.isDeleted }
        val outstandingCredit = invoices.filter { it.paymentStatus == "PENDING" || it.paymentStatus == "CREDIT" }.sumOf { it.total }
        val stockValuation = products.filter { !it.isDeleted }.sumOf { it.stockQty * it.costPrice }

        val snapshot = AnalyticsSnapshot(
            snapshotDate = System.currentTimeMillis(),
            period = "WEEKLY",
            totalSales = totalSales,
            totalOrders = totalOrders,
            avgOrderValue = avgOrderValue,
            totalCustomers = 0,
            totalProducts = products.size,
            lowStockCount = lowStockCount,
            outstandingCredit = outstandingCredit,
            stockValuation = stockValuation
        )
        analyticsSnapshotRepository.save(snapshot)
        return snapshot
    }

    suspend fun generateMonthlySnapshot(): AnalyticsSnapshot {
        val startOfMonth = getStartOfMonth()
        val invoices = invoiceRepository.getAll().first().filter { it.updatedAt >= startOfMonth }
        val products = productRepository.getAll().first()
        val totalSales = invoices.sumOf { it.total }
        val totalOrders = invoices.size
        val avgOrderValue = if (totalOrders > 0) totalSales / totalOrders else 0.0
        val lowStockCount = products.count { it.stockQty <= 5.0 && !it.isDeleted }
        val outstandingCredit = invoices.filter { it.paymentStatus == "PENDING" || it.paymentStatus == "CREDIT" }.sumOf { it.total }
        val stockValuation = products.filter { !it.isDeleted }.sumOf { it.stockQty * it.costPrice }

        val snapshot = AnalyticsSnapshot(
            snapshotDate = System.currentTimeMillis(),
            period = "MONTHLY",
            totalSales = totalSales,
            totalOrders = totalOrders,
            avgOrderValue = avgOrderValue,
            totalCustomers = 0,
            totalProducts = products.size,
            lowStockCount = lowStockCount,
            outstandingCredit = outstandingCredit,
            stockValuation = stockValuation
        )
        analyticsSnapshotRepository.save(snapshot)
        return snapshot
    }

    private fun getStartOfDay(): Long {
        val now = System.currentTimeMillis()
        return now - (now % 86400000)
    }

    private fun getStartOfWeek(): Long {
        val now = System.currentTimeMillis()
        val dayOfWeek = (now / 86400000) % 7
        return now - (dayOfWeek * 86400000)
    }

    private fun getStartOfMonth(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}