package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.DocumentVaultEntity
import com.aimr.aimrpos.data.local.entity.ScanSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentVaultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(doc: DocumentVaultEntity)

    @Query("SELECT * FROM scanned_documents WHERE id = :id")
    suspend fun getById(id: String): DocumentVaultEntity?

    @Query("SELECT * FROM scanned_documents WHERE sessionId = :sessionId AND isDeleted = 0 ORDER BY pageNumber ASC")
    fun getBySession(sessionId: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM scanned_documents WHERE isDeleted = 0 ORDER BY uploadedAt DESC")
    fun getAll(): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM scanned_documents WHERE linkedRecordId = :entityId AND isDeleted = 0")
    fun getByEntity(entityId: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM scanned_documents WHERE documentType = :type AND isDeleted = 0")
    fun getByType(type: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM scanned_documents WHERE locationId = :locationId AND isDeleted = 0")
    fun getByLocation(locationId: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM scanned_documents WHERE extractionStatus = :status AND isDeleted = 0")
    fun getByExtractionStatus(status: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM scanned_documents WHERE processingMode = :mode AND isDeleted = 0")
    fun getByProcessingMode(mode: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM scanned_documents WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<DocumentVaultEntity>>

    @Query("UPDATE scanned_documents SET ocrRawText = :text, extractionStatus = :status, confidence = :confidence WHERE id = :id")
    suspend fun updateOcrResult(id: String, text: String, status: String, confidence: Float)

    @Query("UPDATE scanned_documents SET extractedFieldsCsv = :csv, extractionStatus = :status WHERE id = :id")
    suspend fun updateExtractedCsv(id: String, csv: String, status: String)

    @Query("UPDATE scanned_documents SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("DELETE FROM scanned_documents WHERE id = :id")
    suspend fun deleteById(id: String)
}