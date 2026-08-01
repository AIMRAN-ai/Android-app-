package com.aimr.aimrpos.presentation.supplierledger

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class SupplierLedgerEntry(
    val supplierId: String = "",
    val supplierName: String = "",
    val paymentTerms: String = "Net 30",
    val outstandingBalance: Double = 0.0,
    val totalPaid: Double = 0.0,
    val agingBucket: String = "Current",
    val lastPaymentDate: Long = 0L
)

@Composable
fun SupplierLedgerScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSupplier by remember { mutableStateOf<SupplierLedgerEntry?>(null) }

    val supplierData = listOf(
        SupplierLedgerEntry(
            supplierId = "SUP-001",
            supplierName = "Al-Fatah Trading",
            paymentTerms = "Net 30",
            outstandingBalance = 45000.00,
            totalPaid = 12000.00,
            agingBucket = "31-60 Days",
            lastPaymentDate = System.currentTimeMillis() - 45 * 86400000
        ),
        SupplierLedgerEntry(
            supplierId = "SUP-002",
            supplierName = "Pakistan Steel Ltd",
            paymentTerms = "Net 45",
            outstandingBalance = 82000.50,
            totalPaid = 35000.00,
            agingBucket = "61-90 Days",
            lastPaymentDate = System.currentTimeMillis() - 75 * 86400000
        ),
        SupplierLedgerEntry(
            supplierId = "SUP-003",
            supplierName = "Karachi Chemicals",
            paymentTerms = "Net 15",
            outstandingBalance = 12500.00,
            totalPaid = 8000.00,
            agingBucket = "0-30 Days",
            lastPaymentDate = System.currentTimeMillis() - 10 * 86400000
        ),
        SupplierLedgerEntry(
            supplierId = "SUP-004",
            supplierName = "Lahore Textiles",
            paymentTerms = "Net 30",
            outstandingBalance = 0.0,
            totalPaid = 55000.00,
            agingBucket = "Current",
            lastPaymentDate = System.currentTimeMillis() - 2 * 86400000
        ),
        SupplierLedgerEntry(
            supplierId = "SUP-005",
            supplierName = "Rawal Motors",
            paymentTerms = "Net 60",
            outstandingBalance = 156000.00,
            totalPaid = 20000.00,
            agingBucket = "90+ Days",
            lastPaymentDate = System.currentTimeMillis() - 120 * 86400000
        )
    )

    val filteredSuppliers = if (searchQuery.isEmpty()) supplierData else supplierData.filter {
        it.supplierName.contains(searchQuery, ignoreCase = true) ||
        it.supplierId.contains(searchQuery, ignoreCase = true)
    }

    val totalOutstanding = supplierData.sumOf { it.outstandingBalance }
    val totalPaid = supplierData.sumOf { it.totalPaid }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Supplier Ledger",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search suppliers...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Outstanding", style = MaterialTheme.typography.bodyMedium)
                    Text("Rs ${"%.2f".format(totalOutstanding)}", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Paid", style = MaterialTheme.typography.bodyMedium)
                    Text("Rs ${"%.2f".format(totalPaid)}", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Active Suppliers", style = MaterialTheme.typography.bodyMedium)
                    Text("${supplierData.count { it.outstandingBalance > 0 }}", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Payment Terms Overview", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val terms = supplierData.map { it.paymentTerms }.distinct()
            terms.forEach { term ->
                val count = supplierData.count { it.paymentTerms == term }
                val totalBal = supplierData.filter { it.paymentTerms == term }.sumOf { it.outstandingBalance }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(term, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Text("$count suppliers", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text("Rs ${"%.2f".format(totalBal)}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFD32F2F))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Outstanding Balances", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        filteredSuppliers.forEach { supplier ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(supplier.supplierName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        Text(
                            "Rs ${"%.2f".format(supplier.outstandingBalance)}",
                            color = if (supplier.outstandingBalance > 0) Color(0xFFD32F2F) else Color(0xFF388E3C),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text("ID: ${supplier.supplierId}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Terms: ${supplier.paymentTerms}", style = MaterialTheme.typography.bodySmall)
                        Text("Aging: ${supplier.agingBucket}", style = MaterialTheme.typography.bodySmall,
                            color = when (supplier.agingBucket) {
                                "Current" -> Color(0xFF388E3C)
                                "0-30 Days" -> Color(0xFFFFC107)
                                "31-60 Days" -> Color(0xFFFF9800)
                                "61-90 Days" -> Color(0xFFFF5722)
                                "90+ Days" -> Color(0xFFD32F2F)
                                else -> Color.Gray
                            })
                    }
                    Text("Total Paid: Rs ${"%.2f".format(supplier.totalPaid)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Back")
        }
    }
}