package com.aimr.aimrpos.presentation.pricing

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
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        Text(
            text = "Price Tiers",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Product ID: $productId",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF94A3B8)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.tiers) { tier ->
                PriceTierRow(tier = tier, onDelete = { viewModel.deleteTier(tier.id, productId) })
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
                Text(text = "Min Qty: ${tier.minQty}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                Text(text = "Price: ${tier.price}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                Text(text = tier.customerType, style = MaterialTheme.typography.bodySmall, color = Color(0xFF10B981))
            }
            Button(onClick = { onDelete(tier.id) }, shape = RoundedCornerShape(8.dp)) {
                Text("Delete", color = Color.White)
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
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        Text(
            text = "Price History",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.histories) { history ->
                PriceHistoryRow(history = history)
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
            Text(text = "Price: ${history.oldPrice} → ${history.newPrice}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text(text = "Changed by: ${history.changedBy}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
            Text(text = history.reason ?: "", style = MaterialTheme.typography.bodySmall, color = Color(0xFF10B981))
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
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        Text(
            text = "Promotions",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.promotions) { promotion ->
                PromotionRow(promotion = promotion)
            }
        }
    }
}

@Composable
fun PromotionRow(promotion: com.aimr.aimrpos.domain.model.Promotion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = promotion.name, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = promotion.description ?: "", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
            Text(text = "${promotion.type}: ${promotion.value}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF10B981))
        }
    }
}
