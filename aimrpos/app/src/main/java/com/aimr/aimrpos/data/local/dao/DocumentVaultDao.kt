package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.DocumentVaultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentVaultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(doc: DocumentVaultEntity)

    @Query("SELECT * FROM document_vault WHERE id = :id")
    suspend fun getById(id: String): DocumentVaultEntity?

    @Query("SELECT * FROM document_vault WHERE isDeleted = 0 ORDER BY uploadedAt DESC")
    fun getAll(): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM document_vault WHERE linkedEntityId = :entityId AND isDeleted = 0")
    fun getByEntity(entityId: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM document_vault WHERE documentType = :type AND isDeleted = 0")
    fun getByType(type: String): Flow<List<DocumentVaultEntity>>

    @Query("SELECT * FROM document_vault WHERE syncStatus = 'PENDING'")
    fun getUnsynced(): Flow<List<DocumentVaultEntity>>

    @Query("UPDATE document_vault SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}