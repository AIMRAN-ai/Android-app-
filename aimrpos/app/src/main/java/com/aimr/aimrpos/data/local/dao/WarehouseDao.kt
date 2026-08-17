package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(location: LocationEntity)

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getById(id: String): LocationEntity?

    @Query("SELECT * FROM locations WHERE businessId = :businessId AND isDeleted = 0 ORDER BY name ASC")
    fun getByBusiness(businessId: String): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAll(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isWarehouse = 1 AND isDeleted = 0 ORDER BY name ASC")
    fun getWarehouses(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<LocationEntity>>

    @Query("UPDATE locations SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}