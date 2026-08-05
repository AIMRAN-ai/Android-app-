package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimr.aimrpos.data.local.entity.DashboardWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardWidgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(widget: DashboardWidgetEntity)

    @Query("SELECT * FROM dashboard_widgets WHERE isDeleted = 0 ORDER BY positionY ASC, positionX ASC")
    fun getAllVisible(): Flow<List<DashboardWidgetEntity>>

    @Query("SELECT * FROM dashboard_widgets WHERE id = :id")
    suspend fun getById(id: String): DashboardWidgetEntity?

    @Query("SELECT * FROM dashboard_widgets WHERE isDeleted = 0 AND widgetType = :type")
    fun getByType(type: String): Flow<List<DashboardWidgetEntity>>

    @Query("UPDATE dashboard_widgets SET positionX = :x, positionY = :y, width = :width, height = :height WHERE id = :id")
    suspend fun updateLayout(id: String, x: Int, y: Int, width: Int, height: Int)

    @Query("UPDATE dashboard_widgets SET isVisible = :visible WHERE id = :id")
    suspend fun setVisibility(id: String, visible: Boolean)

    @Query("UPDATE dashboard_widgets SET configJson = :config WHERE id = :id")
    suspend fun updateConfig(id: String, config: String)

    @Query("DELETE FROM dashboard_widgets WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM dashboard_widgets WHERE isDeleted = 1")
    suspend fun deleteSoftDeleted()

    @Query("CREATE INDEX IF NOT EXISTS idx_dashboard_widgets_type ON dashboard_widgets(widgetType)")
    suspend fun indexWidgetType()
}