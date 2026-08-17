package com.aimr.aimrpos.presentation.scale

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
fun ScaleItemScreen(
    navController: NavHostController,
    viewModel: ScaleItemViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadScaleItems()
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
            title = "Scale Items",
            modifier = Modifier.fillMaxWidth(),
            onBack = { navController.popBackStack() }
        )

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
                state.scaleItems.isEmpty() -> {
                    EmptyState(
                        message = "No scale items configured.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.scaleItems) { item ->
                            ScaleItemRow(item = item, onDelete = { viewModel.deleteScaleItem(item.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScaleItemRow(item: com.aimr.aimrpos.domain.model.ScaleItem, onDelete: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Product: ${item.productId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Unit: ${item.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "Conversion Factor: ${item.conversionFactor}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF10B981)
                )
            }
            IconButton(onClick = { onDelete(item.id) }) {
                Text(
                    text = "Delete",
                    color = Color(0xFFEF4444),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
