package com.aimr.aimrpos.presentation.purchase

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
fun PurchaseOrderScreen(navController: NavHostController) {
    var poNumber by remember { mutableStateOf("") }
    var supplierId by remember { mutableStateOf("") }
    var warehouseId by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<POLineItem>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Purchase Order",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = poNumber,
            onValueChange = { poNumber = it },
            label = { Text("PO Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = supplierId,
            onValueChange = { supplierId = it },
            label = { Text("Supplier ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = warehouseId,
            onValueChange = { warehouseId = it },
            label = { Text("Warehouse ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Line Items", style = MaterialTheme.typography.titleLarge)

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
                            label = { Text("Qty") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = item.unitPrice.toString(),
                            onValueChange = { newPrice ->
                                items = items.toMutableList().also {
                                    it[index] = it[index].copy(unitPrice = newPrice.toDoubleOrNull() ?: 0.0)
                                }
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
                val newItem = POLineItem()
                items = items + newItem
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add Line Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val subtotal = items.sumOf { it.quantity * it.unitPrice }
        val taxAmount = subtotal * 0.17
        val total = subtotal + taxAmount

        Text("Subtotal: Rs ${"%.2f".format(subtotal)}", style = MaterialTheme.typography.bodyLarge)
        Text("Tax (17%): Rs ${"%.2f".format(taxAmount)}", style = MaterialTheme.typography.bodyLarge)
        Text("Total: Rs ${"%.2f".format(total)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Submit PO (Pending Approval)")
        }
    }
}

data class POLineItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val taxRate: Double = 17.0,
    val lineTotal: Double = 0.0
)