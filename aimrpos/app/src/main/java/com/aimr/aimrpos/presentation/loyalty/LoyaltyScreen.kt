package com.aimr.aimrpos.presentation.loyalty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.aimr.aimrpos.domain.model.LoyaltyCustomer
import com.aimr.aimrpos.ui.components.EmptyState
import com.aimr.aimrpos.ui.components.LoadingSpinner
import com.aimr.aimrpos.ui.components.TopBar
import kotlinx.coroutines.flow.map

@Composable
fun LoyaltyScreen(
    navController: NavHostController,
    viewModel: LoyaltyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
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
            title = "Loyalty Program",
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
                state.customers.isEmpty() && state.rules.isEmpty() && state.transactions.isEmpty() -> {
                    EmptyState(
                        message = "No loyalty data yet. Start by adding customers and rules.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        if (state.rules.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Loyalty Rules",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(state.rules, key = { it.id }) { rule ->
                                LoyaltyRuleCard(rule = rule)
                            }
                        }

                        if (state.customers.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Top Customers",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(state.customers, key = { it.id }) { customer ->
                                LoyaltyCustomerRow(customer = customer)
                            }
                        }

                        if (state.transactions.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Recent Transactions",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(state.transactions, key = { it.id }) { transaction ->
                                LoyaltyTransactionRow(transaction = transaction)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoyaltyRuleCard(rule: com.aimr.aimrpos.domain.model.LoyaltyRule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = rule.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Points: ${rule.pointsPerAmount.toInt()} per Rs. ${rule.pointsPerAmount.toInt()} min purchase",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = "Min Purchase: Rs ${"%.2f".format(rule.minPurchaseAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF10B981)
            )
        }
    }
}

@Composable
fun LoyaltyCustomerRow(customer: LoyaltyCustomer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Customer #${customer.customerId.take(8)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${customer.pointsBalance} points • ${customer.tier}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF10B981)
                )
            }
        }
    }
}

@Composable
fun LoyaltyTransactionRow(transaction: com.aimr.aimrpos.domain.model.LoyaltyTransaction) {
    val color = if (transaction.type == "EARN") Color(0xFF10B981) else Color(0xFFEF4444)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description ?: "Loyalty Points",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${transaction.type} ${transaction.points} pts",
                    style = MaterialTheme.typography.bodySmall,
                    color = color
                )
            }
        }
    }
}
