package com.aimr.aimrpos.presentation.invoice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Environment
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aimr.aimrpos.utils.ReceiptGenerator
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InvoicePreviewScreen(
    navController: NavHostController,
    invoiceId: String = "",
    viewModel: InvoicePreviewViewModel = viewModel()
) {
    val invoice by viewModel.invoice.collectAsState()
    val items by viewModel.items.collectAsState()

    LaunchedEffect(invoiceId) {
        viewModel.loadInvoice(invoiceId)
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
            text = "Invoice Preview",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Invoice #: ${invoice.invoiceNumber}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Text(
            text = "Date: ${SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(invoice.updatedAt))}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF94A3B8)
        )
        Text(
            text = "Customer: ${invoice.customerName}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        if (!invoice.customerPhone.isNullOrEmpty()) {
            Text(
                text = "Phone: ${invoice.customerPhone}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.productName, modifier = Modifier.weight(1f), color = Color.White)
                Text(text = "${item.quantity} x Rs ${"%.2f".format(item.unitPrice)}", color = Color(0xFF94A3B8))
                Text(text = "Rs ${"%.2f".format(item.lineTotal)}", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Subtotal: Rs ${"%.2f".format(invoice.subtotal)}", color = Color.White)
        Text("Tax (17%): Rs ${"%.2f".format(invoice.taxAmount)}", color = Color.White)
        if (invoice.discount > 0) {
            Text("Discount: Rs ${"%.2f".format(invoice.discount)}", color = Color(0xFFEF4444))
        }
        Text(
            text = "TOTAL: Rs ${"%.2f".format(invoice.total)}",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF10B981),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Payment: ${invoice.paymentMethod}", color = Color.White)
        Text("Status: ${invoice.paymentStatus}", color = Color.White)

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    val receiptGenerator = ReceiptGenerator(navController.context)
                    val file = receiptGenerator.generatePdfReceipt(invoice, items)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Generate PDF Receipt")
            }

            Button(
                onClick = {
                    val receiptGenerator = ReceiptGenerator(navController.context)
                    receiptGenerator.printReceipt(invoice, items)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
            ) {
                Text("Print Receipt")
            }

            Button(
                onClick = {
                    val receiptGenerator = ReceiptGenerator(navController.context)
                    receiptGenerator.shareViaWhatsApp(invoice, items)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
            ) {
                Text("Share via WhatsApp")
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
            ) {
                Text("Back")
            }
        }
    }
}