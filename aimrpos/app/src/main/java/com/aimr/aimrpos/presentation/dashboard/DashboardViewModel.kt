package com.aimr.aimrpos.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.usecase.CheckLowStockUseCase
import com.aimr.aimrpos.domain.usecase.GetDailySalesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardState(
    val todaySales: Double = 0.0,
    val totalOrders: Int = 0,
    val lowStockCount: Int = 0,
    val outstandingCredit: Double = 0.0,
    val stockValuation: Double = 0.0,
    val recentInvoices: List<Invoice> = emptyList()
)

class DashboardViewModel : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun loadDashboardData(products: List<Product>, invoices: List<Invoice>) {
        viewModelScope.launch {
            val lowStockProducts = CheckLowStockUseCase().invoke(products)
            val dailySales = GetDailySalesUseCase().invoke(invoices)
            val outstandingCredit = invoices
                .filter { it.paymentStatus == "PENDING" || it.paymentStatus == "CREDIT" }
                .sumOf { it.total }
            val stockValuation = products.filter { !it.isDeleted }.sumOf { it.stockQty * it.costPrice }

            _state.value = DashboardState(
                todaySales = dailySales.totalSales,
                totalOrders = dailySales.totalOrders,
                lowStockCount = lowStockProducts.size,
                outstandingCredit = outstandingCredit,
                stockValuation = stockValuation,
                recentInvoices = invoices.sortedByDescending { it.updatedAt }.take(5)
            )
        }
    }
}