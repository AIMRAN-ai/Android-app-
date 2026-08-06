package com.aimr.aimrpos.presentation.batch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel

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
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        Text(
            text = "Product Batches",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        productId?.let {
            Text(
                text = "Product ID: $it",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8)
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.batches) { batch ->
                ProductBatchRow(batch = batch, onDelete = { viewModel.deleteBatch(batch.id, productId) })
            }
        }
    }
}

@Composable
fun ProductBatchRow(batch: com.aimr.aimrpos.domain.model.ProductBatch, onDelete: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = batch.batchNumber, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = "Qty: ${batch.quantity}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                Text(text = "Expiry: ${batch.expiryDate}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF10B981))
            }
            Button(onClick = { onDelete(batch.id) }, shape = RoundedCornerShape(8.dp)) {
                Text("Delete", color = Color.White)
            }
        }
    }
}
