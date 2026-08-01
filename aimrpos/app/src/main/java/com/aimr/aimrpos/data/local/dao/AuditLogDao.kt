package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.AuditLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: AuditLogEntity)

    @Query("SELECT * FROM audit_log ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 100): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_log WHERE entityId = :entityId ORDER BY timestamp DESC")
    fun getByEntity(entityId: String): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_log WHERE userId = :userId ORDER BY timestamp DESC")
    fun getByUser(userId: String): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_log WHERE businessId = :businessId ORDER BY timestamp DESC")
    fun getByBusiness(businessId: String): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_log WHERE timestamp >= :start AND timestamp <= :end ORDER BY timestamp DESC")
    fun getByDateRange(start: Long, end: Long): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_log WHERE action = :action ORDER BY timestamp DESC")
    fun getByAction(action: String): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_log WHERE entityType = :type ORDER BY timestamp DESC")
    fun getByEntityType(type: String): Flow<List<AuditLogEntity>>
}