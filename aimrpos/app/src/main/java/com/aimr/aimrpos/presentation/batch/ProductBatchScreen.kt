package com.aimr.aimrpos.presentation.batch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.aimr.aimrpos.ui.components.EmptyState
import com.aimr.aimrpos.ui.components.LoadingSpinner
import com.aimr.aimrpos.ui.components.TopBar

@Composable
fun ProductBatchScreen(
    navController: NavHostController,
    productId: String?,
    viewModel: ProductBatchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadBatches(productId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
    ) {
        TopBar(
            title = "Product Batches",
            modifier = Modifier.fillMaxWidth(),
            onBack = { navController.popBackStack() }
        )

        productId?.let { id ->
            Text(
                text = "Product ID: $id",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    LoadingSpinner(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Text(
                        text = state.error ?: "Unknown error",
                        color = Color(0xFFEF4444),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.batches.isEmpty() -> {
                    EmptyState(
                        message = "No batches found for this product.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.batches) { batch ->
                            ProductBatchRow(batch = batch, onDelete = { viewModel.deleteBatch(batch.id, productId) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductBatchRow(batch: com.aimr.aimrpos.domain.model.ProductBatch, onDelete: (String) -> Unit) {
    val isExpired = batch.expiryDate?.let { expiry ->
        expiry < System.currentTimeMillis()
    } ?: false

    val expiryColor = if (isExpired) Color(0xFFEF4444) else Color(0xFF10B981)
    val expiryText = if (isExpired) "Expired" else "Valid"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = batch.batchNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = expiryText,
                        style = MaterialTheme.typography.bodySmall,
                        color = expiryColor
                    )
                }
                Text(
                    text = "Qty: ${batch.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "Expiry: ${batch.expiryDate?.let { formatDate(it) } ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF10B981)
                )
            }
            IconButton(onClick = { onDelete(batch.id) }) {
                Text(
                    text = "Delete",
                    color = Color(0xFFEF4444),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy")
    return sdf.format(java.util.Date(timestamp))
}
