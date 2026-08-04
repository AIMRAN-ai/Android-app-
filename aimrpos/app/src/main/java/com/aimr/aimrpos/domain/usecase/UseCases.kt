package com.aimr.aimrpos.domain.usecase

import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.InvoiceItem
import com.aimr.aimrpos.domain.model.Product
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GenerateInvoiceNumberUseCase {
    operator fun invoke(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val dateStr = dateFormat.format(Date())
        return "INV-$dateStr-001"
    }
}

class CalculateInvoiceTotalsUseCase {
    operator fun invoke(items: List<InvoiceItem>, discount: Double = 0.0, taxRate: Double = 17.0): InvoiceTotals {
        val subtotal = items.sumOf { it.lineTotal }
        val taxAmount = subtotal * taxRate / 100.0
        val total = subtotal + taxAmount - discount
        return InvoiceTotals(subtotal = subtotal, taxAmount = taxAmount, discount = discount, total = total)
    }
}

data class InvoiceTotals(
    val subtotal: Double,
    val taxAmount: Double,
    val discount: Double,
    val total: Double
)

class CheckLowStockUseCase {
    operator fun invoke(products: List<Product>): List<Product> {
        return products.filter { it.stockQty <= it.lowStockThreshold && !it.isDeleted }
    }
}

class GetDailySalesUseCase {
    operator fun invoke(invoices: List<Invoice>): DailySales {
        val today = System.currentTimeMillis()
        val startOfDay = today - (today % 86400000)
        val todayInvoices = invoices.filter { it.updatedAt >= startOfDay && !it.isDeleted }
        val totalSales = todayInvoices.sumOf { it.total }
        val totalOrders = todayInvoices.size
        val avgOrderValue = if (totalOrders > 0) totalSales / totalOrders else 0.0
        return DailySales(totalSales = totalSales, totalOrders = totalOrders, avgOrderValue = avgOrderValue)
    }
}

data class DailySales(
    val totalSales: Double,
    val totalOrders: Int,
    val avgOrderValue: Double
)

class GetStockValuationUseCase {
    operator fun invoke(products: List<Product>): Double {
        return products.filter { !it.isDeleted }.sumOf { it.stockQty * it.costPrice }
    }
}

class DeductStockUseCase {
    operator fun invoke(products: List<Product>, items: List<InvoiceItem>): List<Product> {
        val productMap = products.associateBy { it.id }.toMutableMap()
        items.forEach { item ->
            val product = productMap[item.productId]
            if (product != null && product.stockQty >= item.quantity) {
                productMap[item.productId] = product.copy(stockQty = product.stockQty - item.quantity)
            }
        }
        return productMap.values.toList()
    }
}

class CalculateCreditBalanceUseCase {
    operator fun invoke(totalInvoices: Double, totalPayments: Double): Double {
        return totalInvoices - totalPayments
    }
}

class SearchProductByBarcodeUseCase {
    operator fun invoke(products: List<Product>, barcode: String): Product? {
        return products.find { it.barcode == barcode && it.isActive }
    }
}

class SearchProductByQrUseCase {
    operator fun invoke(products: List<Product>, qrCode: String): Product? {
        return products.find { it.qrCode == qrCode && it.isActive }
    }
}

class FilterProductsByTypeUseCase {
    operator fun invoke(products: List<Product>, type: String): List<Product> {
        return products.filter { it.productType.equals(type, ignoreCase = true) && it.isActive }
    }
}

class CalculateDiscountUseCase {
    operator fun invoke(amount: Double, discountPercent: Double): Double {
        return amount * discountPercent / 100.0
    }
}

class CalculateTaxUseCase {
    operator fun invoke(amount: Double, taxRate: Double): Double {
        return amount * taxRate / 100.0
    }
}

class ExtensionRegistry {
    private val extensions = mutableMapOf<String, DataSourceExtension>()

    fun register(extension: DataSourceExtension) {
        extensions[extension.id] = extension
    }

    fun getExtension(id: String): DataSourceExtension? = extensions[id]

    fun getAllExtensions(): List<DataSourceExtension> = extensions.values.toList()

    fun loadFromAllSources(products: List<Product>): List<Product> {
        return extensions.values.flatMap { it.loadProducts(products) }
    }
}

interface DataSourceExtension {
    val id: String
    val name: String
    val description: String
    fun loadProducts(existing: List<Product>): List<Product>
}

class CsvImportExtension : DataSourceExtension {
    override val id = "csv_import"
    override val name = "CSV Import"
    override val description = "Import products from CSV file"
    override fun loadProducts(existing: List<Product>): List<Product> {
        return emptyList()
    }
}

class ApiSyncExtension : DataSourceExtension {
    override val id = "api_sync"
    override val name = "API Sync"
    override val description = "Sync products from remote API"
    override fun loadProducts(existing: List<Product>): List<Product> {
        return emptyList()
    }
}

class BarcodeScannerExtension : DataSourceExtension {
    override val id = "barcode_scanner"
    override val name = "Barcode Scanner"
    override val description = "Scan barcodes to add products"
    override fun loadProducts(existing: List<Product>): List<Product> {
        return emptyList()
    }
}

class QrScannerExtension : DataSourceExtension {
    override val id = "qr_scanner"
    override val name = "QR Scanner"
    override val description = "Scan QR codes to add products"
    override fun loadProducts(existing: List<Product>): List<Product> {
        return emptyList()
    }
}

class ManualEntryExtension : DataSourceExtension {
    override val id = "manual_entry"
    override val name = "Manual Entry"
    override val description = "Manually add products"
    override fun loadProducts(existing: List<Product>): List<Product> {
        return emptyList()
    }
}

class HasPermissionUseCase {
    operator fun invoke(userPermissions: List<String>, requiredPermission: String): Boolean {
        return userPermissions.contains(requiredPermission)
    }
}

class ConvertCurrencyUseCase {
    operator fun invoke(amount: Double, fromRate: Double, toRate: Double): Double {
        return amount / fromRate * toRate
    }
}

class EvaluateWorkflowRuleUseCase {
    operator fun invoke(rule: com.aimr.aimrpos.domain.model.WorkflowRule, context: Map<String, Any>): Boolean {
        val conditions = kotlinx.serialization.json.Json.parseToJsonElement(rule.conditionsJson).jsonObject
        return conditions.all { (key, value) ->
            context[key]?.toString() == value.jsonPrimitive.content
        }
    }
}

class GenerateAnalyticsSnapshotUseCase {
    operator fun invoke(
        totalSales: Double,
        totalOrders: Int,
        totalCustomers: Int,
        totalProducts: Int,
        lowStockCount: Int,
        outstandingCredit: Double,
        stockValuation: Double
    ): com.aimr.aimrpos.domain.model.AnalyticsSnapshot {
        val avgOrderValue = if (totalOrders > 0) totalSales / totalOrders else 0.0
        return com.aimr.aimrpos.domain.model.AnalyticsSnapshot(
            snapshotDate = System.currentTimeMillis(),
            period = "DAILY",
            totalSales = totalSales,
            totalOrders = totalOrders,
            avgOrderValue = avgOrderValue,
            totalCustomers = totalCustomers,
            totalProducts = totalProducts,
            lowStockCount = lowStockCount,
            outstandingCredit = outstandingCredit,
            stockValuation = stockValuation
        )
    }
}

class CreateNotificationUseCase {
    operator fun invoke(
        userId: String,
        title: String,
        message: String,
        type: String = "INFO",
        relatedEntityType: String? = null,
        relatedEntityId: String? = null
    ): com.aimr.aimrpos.domain.model.Notification {
        return com.aimr.aimrpos.domain.model.Notification(
            userId = userId,
            title = title,
            message = message,
            type = type,
            relatedEntityType = relatedEntityType,
            relatedEntityId = relatedEntityId
        )
    }
}