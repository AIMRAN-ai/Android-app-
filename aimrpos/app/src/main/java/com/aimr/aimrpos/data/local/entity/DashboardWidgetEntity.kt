package com.aimr.aimrpos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_widgets")
data class DashboardWidgetEntity(
    @PrimaryKey
    val id: String = "",
    val widgetType: String = "",
    val title: String = "",
    val positionX: Int = 0,
    val positionY: Int = 0,
    val width: Int = 1,
    val height: Int = 1,
    val configJson: String = "{}",
    val isVisible: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: String = "PENDING"
)