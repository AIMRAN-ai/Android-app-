package com.aimr.aimrpos.presentation.invoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun InvoicePreviewScreen(
    navController: androidx.navigation.NavHostController,
    invoiceId: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "AIMRAN POS",
            style = MaterialTheme.typography.displayLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "INVOICE",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Invoice #: INV-20260801-001",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Date: 2026-08-01",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Customer: Fatima Khan",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Phone: 0321-9876543",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Item", fontWeight = FontWeight.Bold)
            Text("Qty", fontWeight = FontWeight.Bold)
            Text("Price", fontWeight = FontWeight.Bold)
            Text("Total", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text("Atta 5kg x 2", modifier = Modifier.weight(1f))
        Text("2", modifier = Modifier.weight(1f))
        Text("Rs 420.00", modifier = Modifier.weight(1f))
        Text("Rs 840.00", modifier = Modifier.weight(1f))

        Text("Cooking Oil 1L x 1", modifier = Modifier.weight(1f))
        Text("1", modifier = Modifier.weight(1f))
        Text("Rs 210.00", modifier = Modifier.weight(1f))
        Text("Rs 210.00", modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Subtotal", fontWeight = FontWeight.Medium)
            Text("Rs 1050.00")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Tax (17%)", fontWeight = FontWeight.Medium)
            Text("Rs 178.50")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Discount", fontWeight = FontWeight.Medium)
            Text("Rs 0.00")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("TOTAL", fontWeight = FontWeight.Bold)
            Text("Rs 1228.50", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { /* print */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Print Invoice")
            }

            Button(
                onClick = { /* PDF export */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Export PDF")
            }

            Button(
                onClick = { /* WhatsApp share */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Share via WhatsApp")
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Back")
            }
        }
    }
}