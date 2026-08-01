package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.StockLedgerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockLedgerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: StockLedgerEntity)

    @Query("SELECT * FROM stock_ledger WHERE id = :id")
    suspend fun getById(id: String): StockLedgerEntity?

    @Query("SELECT * FROM stock_ledger WHERE productId = :productId AND locationId = :locationId ORDER BY createdAt DESC")
    fun getByProductAndLocation(productId: String, locationId: String): Flow<List<StockLedgerEntity>>

    @Query("SELECT * FROM stock_ledger WHERE locationId = :locationId ORDER BY createdAt DESC")
    fun getByLocation(locationId: String): Flow<List<StockLedgerEntity>>

    @Query("SELECT * FROM stock_ledger WHERE referenceId = :referenceId")
    fun getByReference(referenceId: String): Flow<List<StockLedgerEntity>>

    @Query("SELECT * FROM stock_ledger WHERE productId = :productId ORDER BY createdAt DESC")
    fun getByProduct(productId: String): Flow<List<StockLedgerEntity>>
}