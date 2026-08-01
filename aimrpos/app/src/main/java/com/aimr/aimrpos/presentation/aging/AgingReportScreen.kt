package com.aimr.aimrpos.presentation.aging

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class AgingEntry(
    val customerName: String = "",
    val invoiceNumber: String = "",
    val invoiceDate: Long = 0L,
    val dueDate: Long = 0L,
    val amount: Double = 0.0,
    val daysOverdue: Int = 0,
    val status: String = "OVERDUE"
)

@Composable
fun AgingReportScreen(navController: NavHostController) {
    val agingData = listOf(
        AgingEntry(
            customerName = "Fatima Khan",
            invoiceNumber = "INV-20260715-001",
            invoiceDate = System.currentTimeMillis() - 30 * 86400000,
            dueDate = System.currentTimeMillis() - 15 * 86400000,
            amount = 2500.0,
            daysOverdue = 15,
            status = "OVERDUE"
        ),
        AgingEntry(
            customerName = "Usman Malik",
            invoiceNumber = "INV-20260720-001",
            invoiceDate = System.currentTimeMillis() - 25 * 86400000,
            dueDate = System.currentTimeMillis() - 10 * 86400000,
            amount = 1800.0,
            daysOverdue = 10,
            status = "OVERDUE"
        ),
        AgingEntry(
            customerName = "Ali Ahmed",
            invoiceNumber = "INV-20260728-001",
            invoiceDate = System.currentTimeMillis() - 5 * 86400000,
            dueDate = System.currentTimeMillis() + 5 * 86400000,
            amount = 150.0,
            daysOverdue = 0,
            status = "CURRENT"
        )
    )

    val totalOutstanding = agingData.filter { it.status == "OVERDUE" }.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Aging Report",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Total Outstanding", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rs ${"%.2f".format(totalOutstanding)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Aging Breakdown:", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0-30 Days", style = MaterialTheme.typography.bodyMedium)
            Text("Rs ${"%.2f".format(agingData.filter { it.daysOverdue in 0..30 }.sumOf { it.amount })}", fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("31-60 Days", style = MaterialTheme.typography.bodyMedium)
            Text("Rs ${"%.2f".format(agingData.filter { it.daysOverdue in 31..60 }.sumOf { it.amount })}", fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("61-90 Days", style = MaterialTheme.typography.bodyMedium)
            Text("Rs ${"%.2f".format(agingData.filter { it.daysOverdue in 61..90 }.sumOf { it.amount })}", fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("90+ Days", style = MaterialTheme.typography.bodyMedium)
            Text("Rs ${"%.2f".format(agingData.filter { it.daysOverdue > 90 }.sumOf { it.amount })}", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(agingData, key = { it.invoiceNumber }) { entry ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(entry.customerName, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                entry.status,
                                color = if (entry.status == "OVERDUE") Color(0xFFD32F2F) else Color(0xFF388E3C),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text("Invoice: ${entry.invoiceNumber}", style = MaterialTheme.typography.bodyMedium)
                        Text("Days Overdue: ${entry.daysOverdue}", style = MaterialTheme.typography.bodySmall)
                        Text("Amount: Rs ${"%.2f".format(entry.amount)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }
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