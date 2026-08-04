package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.SalesForecastEntity
import com.aimr.aimrpos.data.local.entity.AnalyticsSnapshotEntity
import com.aimr.aimrpos.data.local.entity.BusinessUnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecast: SalesForecastEntity)

    @Query("SELECT * FROM sales_forecasts WHERE forecastDate >= :from AND forecastDate <= :to ORDER BY forecastDate ASC")
    fun getBetween(from: Long, to: Long): Flow<List<SalesForecastEntity>>

    @Query("SELECT * FROM sales_forecasts WHERE productId = :productId ORDER BY forecastDate DESC LIMIT :limit")
    fun getForProduct(productId: String, limit: Int = 30): Flow<List<SalesForecastEntity>>

    @Query("SELECT * FROM sales_forecasts WHERE categoryId = :categoryId ORDER BY forecastDate DESC LIMIT :limit")
    fun getForCategory(categoryId: String, limit: Int = 30): Flow<List<SalesForecastEntity>>
}

@Dao
interface AnalyticsSnapshotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(snapshot: AnalyticsSnapshotEntity)

    @Query("SELECT * FROM analytics_snapshots WHERE period = :period ORDER BY snapshotDate DESC LIMIT :limit")
    fun getLatest(period: String = "DAILY", limit: Int = 30): Flow<List<AnalyticsSnapshotEntity>>

    @Query("SELECT * FROM analytics_snapshots WHERE snapshotDate >= :from AND snapshotDate <= :to ORDER BY snapshotDate ASC")
    fun getBetween(from: Long, to: Long): Flow<List<AnalyticsSnapshotEntity>>

    @Query("SELECT * FROM analytics_snapshots ORDER BY snapshotDate DESC LIMIT 1")
    suspend fun getLatestSnapshot(): AnalyticsSnapshotEntity?
}

@Dao
interface BusinessUnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unit: BusinessUnitEntity)

    @Query("SELECT * FROM business_units WHERE businessId = :businessId AND isActive = 1 AND isDeleted = 0")
    fun getActiveForBusiness(businessId: String): Flow<List<BusinessUnitEntity>>

    @Query("SELECT * FROM business_units WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: String): BusinessUnitEntity?

    @Query("UPDATE business_units SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}