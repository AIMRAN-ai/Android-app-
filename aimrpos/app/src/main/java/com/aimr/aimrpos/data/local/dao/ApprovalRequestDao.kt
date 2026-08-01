package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.ApprovalRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApprovalRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(request: ApprovalRequestEntity)

    @Query("SELECT * FROM approval_requests WHERE id = :id")
    suspend fun getById(id: String): ApprovalRequestEntity?

    @Query("SELECT * FROM approval_requests WHERE status = 'PENDING' ORDER BY createdAt ASC")
    fun getPending(): Flow<List<ApprovalRequestEntity>>

    @Query("SELECT * FROM approval_requests WHERE requestedByUserId = :userId ORDER BY createdAt DESC")
    fun getByRequester(userId: String): Flow<List<ApprovalRequestEntity>>

    @Query("SELECT * FROM approval_requests WHERE currentApproverUserId = :userId AND status = 'PENDING'")
    fun getPendingForApprover(userId: String): Flow<List<ApprovalRequestEntity>>

    @Query("SELECT * FROM approval_requests WHERE entityId = :entityId AND entityType = :type")
    fun getByEntity(entityId: String, type: String): Flow<List<ApprovalRequestEntity>>

    @Query("UPDATE approval_requests SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("CREATE INDEX IF NOT EXISTS idx_approval_status ON approval_requests(status)")
    suspend fun indexStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_approval_requester ON approval_requests(requestedByUserId)")
    suspend fun indexRequester()

    @Query("CREATE INDEX IF NOT EXISTS idx_approval_approver ON approval_requests(currentApproverUserId)")
    suspend fun indexApprover()
}