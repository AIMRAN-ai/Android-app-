package com.aimr.aimrpos.presentation.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.usecase.GetDailySalesUseCase
import com.aimr.aimrpos.domain.usecase.GetStockValuationUseCase
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineModel
import com.patrykandpatrick.vico.compose.chart.line.lineSeries
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import kotlinx.coroutines.flow.Flow

@Composable
fun ReportsScreen(
    navController: NavHostController,
    invoicesFlow: Flow<List<Invoice>>,
    productsFlow: Flow<List<com.aimr.aimrpos.domain.model.Product>>
) {
    var invoices by remember { mutableStateOf<List<Invoice>>(emptyList()) }
    var products by remember { mutableStateOf<List<com.aimr.aimrpos.domain.model.Product>>(emptyList()) }
    var timeRange by remember { mutableStateOf("Daily") }

    var totalSales by remember { mutableStateOf(0.0) }
    var totalOrders by remember { mutableStateOf(0) }
    var avgOrderValue by remember { mutableStateOf(0.0) }
    var stockValuation by remember { mutableStateOf(0.0) }
    var profitMargin by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        invoicesFlow.collect { invoiceList ->
            invoices = invoiceList
            val sales = GetDailySalesUseCase().invoke(invoiceList)
            totalSales = sales.totalSales
            totalOrders = sales.totalOrders
            avgOrderValue = sales.avgOrderValue
            profitMargin = totalSales * 0.3
        }
    }

    LaunchedEffect(Unit) {
        productsFlow.collect { productList ->
            products = productList
            stockValuation = GetStockValuationUseCase().invoke(productList)
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Reports & Analytics",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReportFilterChip(
                text = "Daily",
                selected = timeRange == "Daily",
                onClick = { timeRange = "Daily" }
            )
            ReportFilterChip(
                text = "Weekly",
                selected = timeRange == "Weekly",
                onClick = { timeRange = "Weekly" }
            )
            ReportFilterChip(
                text = "Monthly",
                selected = timeRange == "Monthly",
                onClick = { timeRange = "Monthly" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Sales", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                        Text(
                            text = "Rs ${"%.2f".format(totalSales)}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReportMetricCard(
                title = "Total Orders",
                value = totalOrders.toString(),
                color = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
            ReportMetricCard(
                title = "Avg Order Value",
                value = "Rs ${"%.2f".format(avgOrderValue)}",
                color = Color(0xFF8B5CF6),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReportMetricCard(
                title = "Profit Margin",
                value = "Rs ${"%.2f".format(profitMargin)}",
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            ReportMetricCard(
                title = "Stock Valuation",
                value = "Rs ${"%.2f".format(stockValuation)}",
                color = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (invoices.isNotEmpty()) {
            val chartModel = lineModel(
                lineSeries(
                    values = invoices.takeLast(7).mapIndexed { index, invoice ->
                        com.patrykandpatrick.vico.compose.chart.line.lineModel.point(
                            index.toFloat(),
                            invoice.total.toFloat()
                        )
                    },
                    lineSpec = lineSpec(color = Color(0xFF3B82F6))
                )
            )

            Text(
                text = "Sales Trend",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                com.patrykandpatrick.vico.compose.chart.line.LineChart(
                    model = chartModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
fun ReportFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF3B82F6) else Color.Transparent
        ),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (selected) Color.White else Color(0xFF94A3B8)
        )
    }
}

@Composable
fun ReportMetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}