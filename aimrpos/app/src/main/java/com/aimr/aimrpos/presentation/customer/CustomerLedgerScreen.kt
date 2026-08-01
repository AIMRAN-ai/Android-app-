package com.aimr.aimrpos.presentation.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class CustomerLedgerEntry(
    val name: String = "",
    val phone: String = "",
    val creditBalance: Double = 0.0,
    val invoices: List<InvoiceSummary> = emptyList()
)

data class InvoiceSummary(
    val invoiceNumber: String = "",
    val date: String = "",
    val total: Double = 0.0,
    val status: String = "PAID"
)

@Composable
fun CustomerLedgerScreen(navController: androidx.navigation.NavHostController) {
    var searchQuery by remember { mutableStateOf("") }

    val sampleCustomers = listOf(
        CustomerLedgerEntry(
            name = "Ali Ahmed",
            phone = "0300-1234567",
            creditBalance = 0.0,
            invoices = listOf(
                InvoiceSummary("INV-20260801-003", "2026-08-01", 150.0, "PAID")
            )
        ),
        CustomerLedgerEntry(
            name = "Fatima Khan",
            phone = "0321-9876543",
            creditBalance = 2500.0,
            invoices = listOf(
                InvoiceSummary("INV-20260801-001", "2026-08-01", 560.0, "PAID"),
                InvoiceSummary("INV-20260730-001", "2026-07-30", 2500.0, "CREDIT")
            )
        ),
        CustomerLedgerEntry(
            name = "Usman Malik",
            phone = "0345-5551234",
            creditBalance = 1800.0,
            invoices = listOf(
                InvoiceSummary("INV-20260801-002", "2026-08-01", 1240.0, "CREDIT")
            )
        )
    )

    val filteredCustomers = sampleCustomers.filter {
        searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Customer Ledger",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search customers...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredCustomers, key = { it.name }) { customer ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = customer.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Credit: Rs ${"%.2f".format(customer.creditBalance)}",
                                color = if (customer.creditBalance > 0) Color(0xFFD32F2F) else Color(0xFF388E3C),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = customer.phone,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        customer.invoices.forEach { invoice ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(invoice.invoiceNumber, style = MaterialTheme.typography.bodySmall)
                                Text("Rs ${"%.2f".format(invoice.total)}", style = MaterialTheme.typography.bodySmall)
                                Text(invoice.status, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}