package com.aimr.aimrpos.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun InventoryListScreen(navController: androidx.navigation.NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedFilter by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search products...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                text = "All",
                selected = selectedFilter == "All",
                onClick = { selectedFilter = "All" }
            )
            FilterChip(
                text = "In Stock",
                selected = selectedFilter == "In Stock",
                onClick = { selectedFilter = "In Stock" }
            )
            FilterChip(
                text = "Low Stock",
                selected = selectedFilter == "Low Stock",
                onClick = { selectedFilter = "Low Stock" }
            )
            FilterChip(
                text = "Out of Stock",
                selected = selectedFilter == "Out of Stock",
                onClick = { selectedFilter = "Out of Stock" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val sampleProducts = listOf(
            InventoryProduct("1", "Atta 5kg", "ATTA-001", 12.0, 420.0, "pcs", 5),
            InventoryProduct("2", "Sugar 1kg", "SUGR-001", 3.0, 250.0, "kg", 5),
            InventoryProduct("3", "Cooking Oil 1L", "OIL-001", 8.0, 210.0, "liter", 3),
            InventoryProduct("4", "Biscuits 400g", "BISC-001", 2.0, 100.0, "pcs", 5),
            InventoryProduct("5", "Shampoo 200ml", "SHMP-001", 0.0, 85.0, "pcs", 3),
            InventoryProduct("6", "Soap Bar", "SOAP-001", 25.0, 35.0, "pcs", 10),
            InventoryProduct("7", "Pen 12pk", "PEN-001", 5.0, 150.0, "pcs", 3),
            InventoryProduct("8", "Notebook A4", "NBK-001", 15.0, 60.0, "pcs", 5),
            InventoryProduct("9", "Rice 10kg", "RICE-001", 7.0, 950.0, "kg", 3),
            InventoryProduct("10", "Tea 500g", "TEA-001", 1.0, 350.0, "kg", 5)
        )

        val filteredProducts = sampleProducts.filter { product ->
            (selectedFilter == "All" ||
                (selectedFilter == "In Stock" && product.stockQty > product.threshold) ||
                (selectedFilter == "Low Stock" && product.stockQty <= product.threshold && product.stockQty > 0) ||
                (selectedFilter == "Out of Stock" && product.stockQty <= 0)) &&
            (searchQuery.isEmpty() || product.name.contains(searchQuery, ignoreCase = true) || product.sku.contains(searchQuery, ignoreCase = true))
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            filteredProducts.forEach { product ->
                ProductListItem(
                    product = product,
                    onClick = { /* navigate to edit */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("inventory_add_edit") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add Product", fontSize = 16.sp)
        }
    }
}

@Composable
fun ProductListItem(
    product: InventoryProduct,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${product.sku} | ${product.stockQty} ${product.unit}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Rs ${"%.2f".format(product.salePrice)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                StockBadge(
                    stockQty = product.stockQty,
                    threshold = product.threshold
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
        ),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

data class InventoryProduct(
    val id: String,
    val name: String,
    val sku: String,
    val stockQty: Double,
    val salePrice: Double,
    val unit: String,
    val threshold: Double
)