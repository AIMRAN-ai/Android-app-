package com.aimr.aimrpos.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StockBadge(stockQty: Double, threshold: Double) {
    val color = when {
        stockQty <= 0 -> Color(0xFFD32F2F)
        stockQty <= threshold -> Color(0xFFFFC107)
        else -> Color(0xFF388E3C)
    }
    val label = when {
        stockQty <= 0 -> "OUT OF STOCK"
        stockQty <= threshold -> "LOW STOCK"
        else -> "IN STOCK"
    }
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = Color.White,
        modifier = Modifier
            .clip(CircleShape)
            .size(80.dp)
    )
}