package com.aimr.aimrpos.presentation.warehouse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class WarehouseItem(
    val id: String = "",
    val name: String = "",
    val address: String? = null,
    val phone: String? = null,
    val manager: String = ""
)

@Composable
fun WarehouseScreen(navController: NavHostController) {
    var filter by remember { mutableStateOf("All") }

    val sampleWarehouses = listOf(
        WarehouseItem(
            id = "1",
            name = "Main Warehouse",
            address = "Main Bazaar, Lahore",
            phone = "0300-1234567",
            manager = "Owner"
        ),
        WarehouseItem(
            id = "2",
            name = "Branch Store",
            address = "Market Road, Karachi",
            phone = "0321-9876543",
            manager = "Staff"
        ),
        WarehouseItem(
            id = "3",
            name = "Storage Unit",
            address = "Industrial Area, Islamabad",
            phone = "0345-5551234",
            manager = "Staff"
        )
    )

    val filtered = if (filter == "All") sampleWarehouses else sampleWarehouses.filter { it.name.contains(filter, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Warehouses / Locations",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = filter,
            onValueChange = { filter = it },
            label = { Text("Search warehouses...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtered, key = { it.id }) { wh ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(wh.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        Text(wh.address ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        Text("Phone: ${wh.phone ?: "N/A"} | Manager: ${wh.manager}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Back")
        }
    }
}