package com.aimr.aimrpos.presentation.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aimr.aimrpos.domain.model.InvoiceItem
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.usecase.CalculateInvoiceTotalsUseCase
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInvoiceScreen(
    navController: NavHostController,
    productsFlow: Flow<List<Product>>
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("CASH") }
    var discount by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<InvoiceLineItem>()) }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val paymentMethods = listOf("CASH", "BANK", "UPI", "CREDIT", "WALLET", "CHECK")
    val filteredProducts = products.filter { it.isActive }

    LaunchedEffect(Unit) {
        productsFlow.collect { productList ->
            products = productList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "New Invoice",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name", color = Color(0xFF94A3B8)) },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = customerPhone,
            onValueChange = { customerPhone = it },
            label = { Text("Phone", color = Color(0xFF94A3B8)) },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add Items",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedProduct?.name ?: "Select Product",
                onValueChange = {},
                readOnly = true,
                label = { Text("Product", color = Color(0xFF94A3B8)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B)
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredProducts.forEach { product ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(product.name, color = Color.White)
                                Text(
                                    "Stock: ${product.stockQty} ${product.unit} | Rs ${product.salePrice}",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp
                                )
                            }
                        },
                        onClick = {
                            selectedProduct = product
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedProduct != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var quantity by remember { mutableStateOf("1") }
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Qty", color = Color(0xFF94A3B8)) },
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B)
                    )
                )
                Button(
                    onClick = {
                        val qty = quantity.toDoubleOrNull() ?: 0.0
                        if (qty > 0 && qty <= selectedProduct!!.stockQty) {
                            val lineTotal = qty * selectedProduct!!.salePrice
                            items = items + InvoiceLineItem(
                                productName = selectedProduct!!.name,
                                productId = selectedProduct!!.id,
                                quantity = qty.toString(),
                                unitPrice = selectedProduct!!.salePrice.toString(),
                                lineTotal = lineTotal
                            )
                            selectedProduct = null
                            quantity = "1"
                            errorMessage = null
                        } else {
                            errorMessage = "Invalid quantity or insufficient stock"
                        }
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Add")
                }
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = Color(0xFFEF4444),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        items.forEachIndexed { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.productName, color = Color.White, fontWeight = FontWeight.Medium)
                        Text(
                            text = "${item.quantity} x Rs ${"%.2f".format(item.unitPrice)}",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "Rs ${"%.2f".format(item.lineTotal)}",
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = discount,
            onValueChange = { discount = it },
            label = { Text("Discount (Rs)", color = Color(0xFF94A3B8)) },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        val subtotal = items.sumOf { it.lineTotal }
        val taxAmount = subtotal * 0.17
        val discountVal = discount.toDoubleOrNull() ?: 0.0
        val total = subtotal + taxAmount - discountVal

        Text(
            text = "Subtotal: Rs ${"%.2f".format(subtotal)}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Text(
            text = "Tax (17%): Rs ${"%.2f".format(taxAmount)}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        if (discountVal > 0) {
            Text(
                text = "Discount: Rs ${"%.2f".format(discountVal)}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFEF4444)
            )
        }
        Text(
            text = "Total: Rs ${"%.2f".format(total)}",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF10B981),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Payment Method", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            paymentMethods.forEach { method ->
                val isSelected = paymentMethod == method
                Button(
                    onClick = { paymentMethod = method },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B)
                    )
                ) {
                    Text(method, fontSize = 12.sp, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (items.isNotEmpty() && customerName.isNotBlank()) {
                    navController.navigate("invoice_preview")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Save Invoice", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

data class InvoiceLineItem(
    val productName: String = "",
    val productId: String = "",
    val quantity: String = "",
    val unitPrice: String = "",
    val lineTotal: Double = 0.0
)