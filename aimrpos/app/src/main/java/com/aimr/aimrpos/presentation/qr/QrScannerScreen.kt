package com.aimr.aimrpos.presentation.qr

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aimr.aimrpos.data.qr.QrScanner
import com.aimr.aimrpos.data.qr.QrScanResult

@Composable
fun QrScannerScreen(navController: NavHostController) {
    val qrScanner = QrScanner()
    var scanResults by remember { mutableStateOf<List<QrScanResult>>(emptyList()) }
    var isScanning by remember { mutableStateOf(false) }
    var parsedFields by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "QR Scanner",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isScanning) {
                    Text("Scanning...", style = MaterialTheme.typography.bodyLarge)
                } else {
                    Text(
                        text = "📷\nPoint camera at QR code",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    isScanning = true
                    // Simulate QR scan with sample data
                    scanResults = listOf(
                        QrScanResult(
                            rawValue = "{\"name\":\"Fatima Khan\",\"phone\":\"0321-9876543\",\"amount\":\"560.00\",\"invoice\":\"INV-20260801-001\"}",
                            format = "QR_CODE",
                            displayName = "Customer QR",
                            confidence = 0.95f
                        )
                    )
                    parsedFields = qrScanner.parseQrPayload(scanResults.first().rawValue)
                    isScanning = false
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Scan QR")
            }

            Button(
                onClick = {
                    scanResults = emptyList()
                    parsedFields = emptyMap()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Clear")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (scanResults.isNotEmpty()) {
            Text("Scan Results:", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            scanResults.forEach { result ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Format: ${result.format}", style = MaterialTheme.typography.bodyMedium)
                        Text("Confidence: ${(result.confidence * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
                        Text("Raw: ${result.rawValue.take(100)}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (parsedFields.isNotEmpty()) {
                Text("Parsed Fields:", style = MaterialTheme.typography.titleLarge)
                parsedFields.forEach { (key, value) ->
                    Text("$key: $value", style = MaterialTheme.typography.bodyMedium)
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