package com.aimr.aimrpos.presentation.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.InvoiceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InvoicePreviewViewModel : ViewModel() {
    private val _invoice = MutableStateFlow(Invoice())
    val invoice: StateFlow<Invoice> = _invoice.asStateFlow()

    private val _items = MutableStateFlow<List<InvoiceItem>>(emptyList())
    val items: StateFlow<List<InvoiceItem>> = _items.asStateFlow()

    fun loadInvoice(invoiceId: String) {
        viewModelScope.launch {
            val invoice = Invoice(
                id = invoiceId,
                invoiceNumber = "INV-${System.currentTimeMillis()}",
                customerName = "Guest Customer",
                subtotal = 0.0,
                taxAmount = 0.0,
                discount = 0.0,
                total = 0.0,
                paymentStatus = "PENDING",
                paymentMethod = "CASH",
                createdByUserId = "system",
                updatedAt = System.currentTimeMillis()
            )
            _invoice.value = invoice
            _items.value = emptyList()
        }
    }
}