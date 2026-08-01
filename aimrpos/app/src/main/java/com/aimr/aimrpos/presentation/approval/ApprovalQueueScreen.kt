package com.aimr.aimrpos.presentation.approval

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class ApprovalItem(
    val id: String = "",
    val requestType: String = "",
    val entityId: String = "",
    val entityType: String = "",
    val requestedBy: String = "",
    val status: String = "PENDING",
    val createdAt: Long = 0L
)

@Composable
fun ApprovalQueueScreen(navController: NavHostController) {
    val pendingApprovals = listOf(
        ApprovalItem(
            id = "1",
            requestType = "PURCHASE_ORDER",
            entityId = "PO-001",
            entityType = "PurchaseOrder",
            requestedBy = "Staff User",
            status = "PENDING",
            createdAt = System.currentTimeMillis()
        ),
        ApprovalItem(
            id = "2",
            requestType = "RETURN_INVOICE",
            entityId = "RET-001",
            entityType = "ReturnInvoice",
            requestedBy = "Staff User",
            status = "PENDING",
            createdAt = System.currentTimeMillis() - 3600000
        ),
        ApprovalItem(
            id = "3",
            requestType = "STOCK_TRANSFER",
            entityId = "TRF-001",
            entityType = "StockTransfer",
            requestedBy = "Warehouse Staff",
            status = "PENDING",
            createdAt = System.currentTimeMillis() - 7200000
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Approval Queue",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${pendingApprovals.size} pending approvals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pendingApprovals, key = { it.id }) { item ->
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
                                text = item.requestType,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = item.status,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text("Entity: ${item.entityType} - ${item.entityId}", style = MaterialTheme.typography.bodyMedium)
                        Text("Requested by: ${item.requestedBy}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { /* approve */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Approve")
                            }

                            Button(
                                onClick = { /* reject */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Reject")
                            }
                        }
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