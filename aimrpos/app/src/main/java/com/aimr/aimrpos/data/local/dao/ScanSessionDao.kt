package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.ScanSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: ScanSessionEntity)

    @Query("SELECT * FROM scan_sessions WHERE id = :id")
    suspend fun getById(id: String): ScanSessionEntity?

    @Query("SELECT * FROM scan_sessions WHERE isDeleted = 0 ORDER BY scannedAt DESC")
    fun getAll(): Flow<List<ScanSessionEntity>>

    @Query("SELECT * FROM scan_sessions WHERE isDeleted = 0 AND documentType = :type ORDER BY scannedAt DESC")
    fun getByType(type: String): Flow<List<ScanSessionEntity>>

    @Query("SELECT * FROM scan_sessions WHERE isDeleted = 0 AND scannedByUserId = :userId ORDER BY scannedAt DESC")
    fun getByUser(userId: String): Flow<List<ScanSessionEntity>>

    @Query("SELECT * FROM scan_sessions WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<ScanSessionEntity>>

    @Query("UPDATE scan_sessions SET ocrRawText = :ocrRawText, extractionStatus = :status, confidence = :confidence WHERE id = :id")
    suspend fun updateOcrResult(id: String, ocrRawText: String, status: String, confidence: Float)

    @Query("UPDATE scan_sessions SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("DELETE FROM scan_sessions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM scan_sessions WHERE isDeleted = 1")
    suspend fun deleteSoftDeleted()

    @Query("CREATE INDEX IF NOT EXISTS idx_scan_sessions_sync_status ON scan_sessions(syncStatus)")
    suspend fun indexSyncStatus()

    @Query("CREATE INDEX IF NOT EXISTS idx_scan_sessions_document_type ON scan_sessions(documentType)")
    suspend fun indexDocumentType()

    @Query("CREATE INDEX IF NOT EXISTS idx_scan_sessions_scanned_at ON scan_sessions(scannedAt)")
    suspend fun indexScannedAt()
}