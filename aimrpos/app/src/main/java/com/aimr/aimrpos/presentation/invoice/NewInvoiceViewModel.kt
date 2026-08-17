package com.aimr.aimrpos.presentation.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.InvoiceItem
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.usecase.CalculateInvoiceTotalsUseCase
import com.aimr.aimrpos.domain.usecase.DeductStockUseCase
import com.aimr.aimrpos.domain.usecase.GenerateInvoiceNumberUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InvoiceLineItem(
    val productName: String = "",
    val productId: String = "",
    val quantity: String = "",
    val unitPrice: String = "",
    val lineTotal: Double = 0.0
)

class NewInvoiceViewModel : ViewModel() {
    private val _customerName = MutableStateFlow("")
    val customerName: StateFlow<String> = _customerName.asStateFlow()

    private val _customerPhone = MutableStateFlow("")
    val customerPhone: StateFlow<String> = _customerPhone.asStateFlow()

    private val _paymentMethod = MutableStateFlow("CASH")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    private val _discount = MutableStateFlow("")
    val discount: StateFlow<String> = _discount.asStateFlow()

    private val _items = MutableStateFlow<List<InvoiceLineItem>>(emptyList())
    val items: StateFlow<List<InvoiceLineItem>> = _items.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _invoiceSaved = MutableStateFlow(false)
    val invoiceSaved: StateFlow<Boolean> = _invoiceSaved.asStateFlow()

    fun setCustomerName(name: String) { _customerName.value = name }
    fun setCustomerPhone(phone: String) { _customerPhone.value = phone }
    fun setPaymentMethod(method: String) { _paymentMethod.value = method }
    fun setDiscount(discount: String) { _discount.value = discount }
    fun setProducts(products: List<Product>) { _products.value = products }

    fun addItem(product: Product, quantity: String) {
        val qty = quantity.toDoubleOrNull() ?: 0.0
        if (qty <= 0) {
            _errorMessage.value = "Quantity must be greater than 0"
            return
        }
        if (qty > product.stockQty) {
            _errorMessage.value = "Insufficient stock. Available: ${product.stockQty}"
            return
        }
        val lineTotal = qty * product.salePrice
        _items.value = _items.value + InvoiceLineItem(
            productName = product.name,
            productId = product.id,
            quantity = quantity,
            unitPrice = product.salePrice.toString(),
            lineTotal = lineTotal
        )
        _errorMessage.value = null
    }

    fun removeItem(productId: String) {
        _items.value = _items.value.filter { it.productId != productId }
    }

    fun saveInvoice(onSuccess: (Invoice) -> Unit) {
        viewModelScope.launch {
            val items = _items.value
            if (items.isEmpty()) {
                _errorMessage.value = "Add at least one item"
                return@launch
            }
            if (_customerName.value.isBlank()) {
                _errorMessage.value = "Enter customer name"
                return@launch
            }

            val invoiceItems = items.map { item ->
                InvoiceItem(
                    productId = item.productId,
                    productName = item.productName,
                    quantity = item.quantity.toDouble(),
                    unitPrice = item.unitPrice.toDouble(),
                    lineTotal = item.lineTotal
                )
            }

            val totals = CalculateInvoiceTotalsUseCase().invoke(
                items = invoiceItems,
                discount = _discount.value.toDoubleOrNull() ?: 0.0
            )

            val invoice = Invoice(
                invoiceNumber = GenerateInvoiceNumberUseCase().invoke(),
                customerId = null,
                customerName = _customerName.value,
                customerPhone = _customerPhone.value.ifBlank { null },
                subtotal = totals.subtotal,
                taxAmount = totals.taxAmount,
                discount = totals.discount,
                total = totals.total,
                paymentStatus = if (_paymentMethod.value == "CREDIT") "CREDIT" else "PENDING",
                paymentMethod = _paymentMethod.value,
                createdByUserId = "current_user",
                updatedAt = System.currentTimeMillis()
            )

            DeductStockUseCase().invoke(_products.value, invoiceItems)

            _invoiceSaved.value = true
            onSuccess(invoice)
        }
    }

    fun clearInvoice() {
        _customerName.value = ""
        _customerPhone.value = ""
        _paymentMethod.value = "CASH"
        _discount.value = ""
        _items.value = emptyList()
        _errorMessage.value = null
        _invoiceSaved.value = false
    }
}