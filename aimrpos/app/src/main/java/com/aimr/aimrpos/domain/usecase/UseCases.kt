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