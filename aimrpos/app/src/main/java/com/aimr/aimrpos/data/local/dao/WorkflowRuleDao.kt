package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.WorkflowRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkflowRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rule: WorkflowRuleEntity)

    @Query("SELECT * FROM workflow_rules WHERE isActive = 1 AND isDeleted = 0 ORDER BY createdAt ASC")
    fun getActiveRules(): Flow<List<WorkflowRuleEntity>>

    @Query("SELECT * FROM workflow_rules WHERE entityType = :entityType AND action = :action AND isActive = 1 AND isDeleted = 0 LIMIT 1")
    suspend fun getRule(entityType: String, action: String): WorkflowRuleEntity?

    @Query("UPDATE workflow_rules SET isActive = :isActive WHERE id = :id")
    suspend fun setActive(id: String, isActive: Boolean)

    @Query("UPDATE workflow_rules SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}