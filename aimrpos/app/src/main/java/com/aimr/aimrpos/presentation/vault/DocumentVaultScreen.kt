package com.aimr.aimrpos.presentation.vault

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

data class VaultDocument(
    val id: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val uploadedAt: Long = 0L,
    val confidence: Float = 0.0f,
    val linkedEntity: String? = null
)

@Composable
fun DocumentVaultScreen(navController: NavHostController) {
    var filterType by remember { mutableStateOf("All") }

    val sampleDocs = listOf(
        VaultDocument(
            id = "1",
            documentType = "INVOICE",
            fileName = "invoice_20260801.pdf",
            uploadedAt = System.currentTimeMillis(),
            confidence = 0.92f,
            linkedEntity = "INV-001"
        ),
        VaultDocument(
            id = "2",
            documentType = "RECEIPT",
            fileName = "receipt_supplier_001.jpg",
            uploadedAt = System.currentTimeMillis() - 86400000,
            confidence = 0.88f,
            linkedEntity = "PO-001"
        ),
        VaultDocument(
            id = "3",
            documentType = "CNIC",
            fileName = "cnic_supplier_abc.png",
            uploadedAt = System.currentTimeMillis() - 172800000,
            confidence = 0.95f,
            linkedEntity = "SUP-001"
        ),
        VaultDocument(
            id = "4",
            documentType = "DELIVERY_NOTE",
            fileName = "dn_20260730.pdf",
            uploadedAt = System.currentTimeMillis() - 259200000,
            confidence = 0.78f,
            linkedEntity = "GRN-001"
        )
    )

    val filteredDocs = if (filterType == "All") sampleDocs else sampleDocs.filter { it.documentType == filterType }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Document Vault",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VaultFilterChip(
                text = "All",
                selected = filterType == "All",
                onClick = { filterType = "All" }
            )
            VaultFilterChip(
                text = "Invoice",
                selected = filterType == "INVOICE",
                onClick = { filterType = "INVOICE" }
            )
            VaultFilterChip(
                text = "Receipt",
                selected = filterType == "RECEIPT",
                onClick = { filterType = "RECEIPT" }
            )
            VaultFilterChip(
                text = "CNIC",
                selected = filterType == "CNIC",
                onClick = { filterType = "CNIC" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            filteredDocs.forEach { doc ->
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
                                text = doc.fileName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${(doc.confidence * 100).toInt()}%",
                                color = if (doc.confidence >= 0.8) Color(0xFF388E3C) else Color(0xFFFFC107)
                            )
                        }
                        Text("Type: ${doc.documentType}", style = MaterialTheme.typography.bodyMedium)
                        Text("Linked: ${doc.linkedEntity ?: "None"}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
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

@Composable
fun VaultFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
        ),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}