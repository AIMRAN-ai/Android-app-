package com.aimr.aimrpos.presentation.pricing

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
fun PriceTierScreen(
    navController: NavHostController,
    productId: String,
    viewModel: PriceTierViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadTiers(productId)
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
            title = "Price Tiers",
            modifier = Modifier.fillMaxWidth(),
            onBack = { navController.popBackStack() }
        )

        Text(
            text = "Product ID: $productId",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
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
                state.tiers.isEmpty() -> {
                    EmptyState(
                        message = "No price tiers configured for this product.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.tiers) { tier ->
                            PriceTierRow(tier = tier, onDelete = { viewModel.deleteTier(tier.id, productId) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PriceTierRow(tier: com.aimr.aimrpos.domain.model.PriceTier, onDelete: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Min Qty: ${tier.minQty}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Text(
                    text = "Price: ${tier.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = tier.customerType,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF10B981)
                )
            }
            androidx.compose.material3.IconButton(onClick = { onDelete(tier.id) }) {
                Text(
                    text = "Delete",
                    color = Color(0xFFEF4444),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun PriceHistoryScreen(
    navController: NavHostController,
    productId: String?,
    viewModel: PriceHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(productId) {
        productId?.let { viewModel.loadHistory(it) }
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
            title = "Price History",
            modifier = Modifier.fillMaxWidth(),
            onBack = { navController.popBackStack() }
        )

        productId?.let {
            Text(
                text = "Product ID: $it",
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
                state.histories.isEmpty() -> {
                    EmptyState(
                        message = "No price history available.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.histories) { history ->
                            PriceHistoryRow(history = history)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PriceHistoryRow(history: com.aimr.aimrpos.domain.model.PriceHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Price: ${history.oldPrice} → ${history.newPrice}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = if (history.newPrice > (history.oldPrice ?: 0)) Color(0xFFEF4444) else Color(0xFF10B981)
            )
            Text(
                text = "Changed by: ${history.changedBy}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
            history.reason?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF10B981)
                )
            }
        }
    }
}

@Composable
fun PromotionScreen(
    navController: NavHostController,
    viewModel: PromotionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPromotions()
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
            title = "Promotions",
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
                state.promotions.isEmpty() -> {
                    EmptyState(
                        message = "No promotions configured.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.promotions) { promotion ->
                            PromotionRow(promotion = promotion)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PromotionRow(promotion: com.aimr.aimrpos.domain.model.Promotion) {
    val isActive = promotion.endDate?.let { endDate ->
        endDate > System.currentTimeMillis()
    } ?: true

    val statusColor = if (isActive) Color(0xFF10B981) else Color(0xFFEF4444)
    val statusText = if (isActive) "Active" else "Expired"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = promotion.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor
                )
            }
            promotion.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
            }
            Text(
                text = "${promotion.type}: ${promotion.value}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF10B981)
            )
        }
    }
}
