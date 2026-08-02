package com.aimr.aimrpos.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aimr.aimrpos.domain.model.Product
import com.aimr.aimrpos.domain.model.Invoice
import kotlinx.coroutines.flow.Flow

@Composable
fun DashboardScreen(
    navController: NavHostController,
    productsFlow: Flow<List<Product>>,
    invoicesFlow: Flow<List<Invoice>>,
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var products by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<List<Product>>(emptyList()) }
    var invoices by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<List<Invoice>>(emptyList()) }

    LaunchedEffect(Unit) {
        productsFlow.collect { productList ->
            products = productList
            viewModel.loadDashboardData(productList, invoices)
        }
    }

    LaunchedEffect(Unit) {
        invoicesFlow.collect { invoiceList ->
            invoices = invoiceList
            viewModel.loadDashboardData(products, invoiceList)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = "AIMRAN POS",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Enterprise Dashboard",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF94A3B8),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardCard(
                title = "Today's Sales",
                value = "Rs ${"%.2f".format(state.todaySales)}",
                icon = Icons.Default.AttachMoney,
                iconBg = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            DashboardCard(
                title = "Invoices",
                value = state.totalOrders.toString(),
                icon = Icons.Default.ShoppingCart,
                iconBg = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardCard(
                title = "Low Stock",
                value = state.lowStockCount.toString(),
                icon = Icons.Default.Inventory,
                iconBg = if (state.lowStockCount > 0) Color(0xFFEF4444) else Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            DashboardCard(
                title = "Outstanding Credit",
                value = "Rs ${"%.2f".format(state.outstandingCredit)}",
                icon = Icons.Default.People,
                iconBg = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionButton(
                text = "New Invoice",
                icon = Icons.Default.ShoppingCart,
                onClick = { navController.navigate("invoice_new") }
            )
            ActionButton(
                text = "Scan Product",
                icon = Icons.Default.Inventory,
                onClick = { navController.navigate("scan_hub") }
            )
            ActionButton(
                text = "View Reports",
                icon = Icons.Default.AttachMoney,
                onClick = { navController.navigate("reports") }
            )
            ActionButton(
                text = "Manage Inventory",
                icon = Icons.Default.Inventory,
                onClick = { navController.navigate("inventory") }
            )
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconBg,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}