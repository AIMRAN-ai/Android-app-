package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.WarehouseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WarehouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(warehouse: WarehouseEntity)

    @Query("SELECT * FROM warehouses WHERE id = :id")
    suspend fun getById(id: String): WarehouseEntity?

    @Query("SELECT * FROM warehouses WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAll(): Flow<List<WarehouseEntity>>

    @Query("SELECT * FROM warehouses WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<WarehouseEntity>>

    @Query("UPDATE warehouses SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}