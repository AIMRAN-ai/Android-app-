package com.aimr.aimrpos.presentation.scanhub

import android.graphics.Bitmap
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.model.ProductType
import com.aimr.aimrpos.domain.usecase.SearchProductByBarcodeUseCase
import com.aimr.aimrpos.domain.usecase.SearchProductByQrUseCase

enum class DocumentType(val label: String, val route: String) {
    INVOICE("Invoice", "document_scanner"),
    RECEIPT("Receipt", "document_scanner"),
    CNIC("CNIC", "document_scanner"),
    CHEQUE("Cheque", "document_scanner"),
    DELIVERY_NOTE("Delivery Note", "document_scanner"),
    OTHER("Other", "document_scanner")
}

@Composable
fun ScanHubScreen(
    navController: NavHostController,
    onProductScanned: (Product) -> Unit = {}
) {
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedType by remember { mutableStateOf(DocumentType.INVOICE) }
    var isProcessing by remember { mutableStateOf(false) }
    var scannedProduct by remember { mutableStateOf<Product?>(null) }
    var scanMethod by remember { mutableStateOf("CAMERA") }

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
            text = "Scan Hub",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select scan method",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScanMethodButton(
                text = "Camera",
                selected = scanMethod == "CAMERA",
                onClick = { scanMethod = "CAMERA" }
            )
            ScanMethodButton(
                text = "Barcode",
                selected = scanMethod == "BARCODE",
                onClick = { scanMethod = "BARCODE" }
            )
            ScanMethodButton(
                text = "QR Code",
                selected = scanMethod == "QR",
                onClick = { scanMethod = "QR" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Document Type",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DocumentType.values().forEach { type ->
                val isSelected = selectedType == type
                OutlinedButton(
                    onClick = { selectedType = type },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isSelected) Color.White else Color(0xFF94A3B8)
                    )
                ) {
                    Text(
                        text = type.label,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (capturedBitmap != null) {
                    Text(
                        text = "Image captured (${capturedBitmap!!.width}x${capturedBitmap!!.height})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Camera preview will appear here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    capturedBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                    croppedBitmap = capturedBitmap
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Capture", fontSize = 14.sp)
            }

            Button(
                onClick = {
                    if (capturedBitmap != null) {
                        isProcessing = true
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
            ) {
                Text("Auto-Crop & Scan", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isProcessing) {
            Text(
                text = "Processing and auto-cropping...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFF59E0B)
            )
        }

        if (croppedBitmap != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Auto-crop preview ready — ${selectedType.label}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF10B981)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("document_scanner") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
            ) {
                Text("Open Scanner for OCR")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
fun ScanMethodButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF3B82F6) else Color.Transparent
        ),
        modifier = Modifier.height(40.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (selected) Color.White else Color(0xFF94A3B8)
        )
    }
}