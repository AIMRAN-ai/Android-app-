package com.aimr.aimrpos.presentation.returninvoice

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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ReturnInvoiceScreen(navController: NavHostController) {
    var returnNumber by remember { mutableStateOf("") }
    var originalInvoiceId by remember { mutableStateOf("") }
    var customerId by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<ReturnLineItem>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Return / Refund",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = returnNumber,
            onValueChange = { returnNumber = it },
            label = { Text("Return Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = originalInvoiceId,
            onValueChange = { originalInvoiceId = it },
            label = { Text("Original Invoice ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = customerId,
            onValueChange = { customerId = it },
            label = { Text("Customer ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("Reason for Return") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Return Items", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

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
                            value = item.quantity.toString(),
                            onValueChange = { newQty ->
                                items = items.toMutableList().also {
                                    it[index] = it[index].copy(quantity = newQty.toDoubleOrNull() ?: 0.0)
                                }
                            },
                            label = { Text("Qty Returned") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = item.unitPrice.toString(),
                            onValueChange = { newPrice ->
                                items = items.toMutableList().also {
                                    it[index] = it[index].copy(unitPrice = newPrice.toDoubleOrNull() ?: 0.0)
                                }
                            },
                            label = { Text("Unit Price") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val newItem = ReturnLineItem()
                items = items + newItem
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add Return Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val subtotal = items.sumOf { it.quantity * it.unitPrice }
        val taxAmount = subtotal * 0.17
        val total = subtotal + taxAmount

        Text("Subtotal: Rs ${"%.2f".format(subtotal)}", style = MaterialTheme.typography.bodyLarge)
        Text("Tax (17%): Rs ${"%.2f".format(taxAmount)}", style = MaterialTheme.typography.bodyLarge)
        Text("Total Refund: Rs ${"%.2f".format(total)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Submit Return (Pending Approval)")
        }
    }
}

data class ReturnLineItem(
    val invoiceItemId: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val lineTotal: Double = 0.0
)