package com.aimr.aimrpos.presentation.invoice

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class InvoiceLineItem(
    val productName: String = "",
    val quantity: String = "",
    val unitPrice: String = "",
    val taxRate: String = "17",
    val lineTotal: Double = 0.0
)

@Composable
fun NewInvoiceScreen(navController: androidx.navigation.NavHostController) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("CASH") }
    var discount by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<InvoiceLineItem>()) }

    val sampleProducts = listOf("Atta 5kg", "Sugar 1kg", "Cooking Oil 1L", "Biscuits 400g", "Shampoo 200ml")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "New Invoice",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = customerPhone,
            onValueChange = { customerPhone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Items",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (items.isEmpty()) {
            Text(
                text = "No items added yet",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        items.forEachIndexed { index, item ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(
                        value = item.productName,
                        onValueChange = { newName ->
                            items = items.toMutableList().also { it[index] = it[index].copy(productName = newName) }
                        },
                        label = { Text("Product") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = item.quantity,
                            onValueChange = { newQty ->
                                items = items.toMutableList().also { it[index] = it[index].copy(quantity = newQty) }
                            },
                            label = { Text("Qty") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = item.unitPrice,
                            onValueChange = { newPrice ->
                                items = items.toMutableList().also { it[index] = it[index].copy(unitPrice = newPrice) }
                            },
                            label = { Text("Price") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val newItem = InvoiceLineItem()
                items = items + newItem
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = discount,
            onValueChange = { discount = it },
            label = { Text("Discount (Rs)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val subtotal = items.sumOf { it.lineTotal }
        val taxAmount = subtotal * 0.17
        val total = subtotal + taxAmount - discount.toDoubleOrNull().orDefault(0.0)

        Text(
            text = "Subtotal: Rs ${"%.2f".format(subtotal)}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Tax (17%): Rs ${"%.2f".format(taxAmount)}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Total: Rs ${"%.2f".format(total)}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = paymentMethod,
            onValueChange = { paymentMethod = it },
            label = { Text("Payment Method (CASH / BANK / UPI / CREDIT)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("invoice_preview")
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Save Invoice", fontSize = 16.sp)
        }
    }
}