package com.aimr.aimrpos.presentation.audit

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class AuditLogEntry(
    val id: String = "",
    val userName: String = "",
    val action: String = "",
    val entityType: String = "",
    val entityId: String = "",
    val timestamp: Long = 0L,
    val deviceId: String = ""
)

@Composable
fun AuditLogScreen(navController: NavHostController) {
    var filterAction by remember { mutableStateOf("All") }

    val sampleLogs = listOf(
        AuditLogEntry(
            id = "1",
            userName = "Owner",
            action = "CREATE",
            entityType = "Product",
            entityId = "PROD-001",
            timestamp = System.currentTimeMillis(),
            deviceId = "DEVICE-001"
        ),
        AuditLogEntry(
            id = "2",
            userName = "Staff",
            action = "UPDATE",
            entityType = "Invoice",
            entityId = "INV-001",
            timestamp = System.currentTimeMillis() - 3600000,
            deviceId = "DEVICE-001"
        ),
        AuditLogEntry(
            id = "3",
            userName = "Owner",
            action = "APPROVE",
            entityType = "PurchaseOrder",
            entityId = "PO-001",
            timestamp = System.currentTimeMillis() - 7200000,
            deviceId = "DEVICE-001"
        ),
        AuditLogEntry(
            id = "4",
            userName = "Staff",
            action = "DELETE",
            entityType = "Customer",
            entityId = "CUST-001",
            timestamp = System.currentTimeMillis() - 86400000,
            deviceId = "DEVICE-002"
        )
    )

    val filteredLogs = if (filterAction == "All") sampleLogs else sampleLogs.filter { it.action == filterAction }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Audit Log",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AuditFilterChip(
                text = "All",
                selected = filterAction == "All",
                onClick = { filterAction = "All" }
            )
            AuditFilterChip(
                text = "CREATE",
                selected = filterAction == "CREATE",
                onClick = { filterAction = "CREATE" }
            )
            AuditFilterChip(
                text = "UPDATE",
                selected = filterAction == "UPDATE",
                onClick = { filterAction = "UPDATE" }
            )
            AuditFilterChip(
                text = "DELETE",
                selected = filterAction == "DELETE",
                onClick = { filterAction = "DELETE" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            filteredLogs.forEach { log ->
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
                                text = log.action,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = when (log.action) {
                                    "CREATE" -> Color(0xFF388E3C)
                                    "UPDATE" -> Color(0xFF1565C0)
                                    "DELETE" -> Color(0xFFD32F2F)
                                    "APPROVE" -> Color(0xFFFF8F00)
                                    else -> Color.Gray
                                }
                            )
                            Text(
                                text = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", log.timestamp).toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Text("${log.userName} → ${log.entityType}: ${log.entityId}", style = MaterialTheme.typography.bodyMedium)
                        Text("Device: ${log.deviceId}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
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
fun AuditFilterChip(
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