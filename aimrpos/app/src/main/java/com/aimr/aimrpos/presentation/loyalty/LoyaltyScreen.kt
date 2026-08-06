package com.aimr.aimrpos.presentation.loyalty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.aimr.aimrpos.domain.model.LoyaltyCustomer

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
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        Text(
            text = "Loyalty Program",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Loyalty Rules",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.rules) { rule ->
                LoyaltyRuleCard(rule = rule)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Top Customers",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.customers) { customer ->
                LoyaltyCustomerRow(customer = customer)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.transactions) { transaction ->
                LoyaltyTransactionRow(transaction = transaction)
            }
        }
    }
}

@Composable
fun LoyaltyRuleCard(rule: com.aimr.aimrpos.domain.model.LoyaltyRule) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = rule.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Pts: ${rule.pointsPerAmount.toInt()} per Rs ${rule.pointsPerAmount.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = "Min: Rs ${"%.2f".format(rule.minPurchaseAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF10B981)
            )
        }
    }
}

@Composable
fun LoyaltyCustomerRow(customer: LoyaltyCustomer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
        modifier = Modifier.fillMaxWidth(),
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
