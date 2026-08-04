package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.CurrencyEntity
import com.aimr.aimrpos.data.local.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(currency: CurrencyEntity)

    @Query("SELECT * FROM currencies ORDER BY code ASC")
    fun getAll(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currencies WHERE code = :code LIMIT 1")
    suspend fun getByCode(code: String): CurrencyEntity?

    @Query("SELECT * FROM currencies WHERE isBaseCurrency = 1 LIMIT 1")
    suspend fun getBaseCurrency(): CurrencyEntity?

    @Query("UPDATE currencies SET exchangeRate = :rate, updatedAt = :updatedAt WHERE code = :code")
    suspend fun updateRate(code: String, rate: Double, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE currencies SET syncStatus = :status WHERE code = :code")
    suspend fun updateSyncStatus(code: String, status: String)
}

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rate: ExchangeRateEntity)

    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :from AND toCurrency = :to AND validTo IS NULL ORDER BY validFrom DESC LIMIT 1")
    suspend fun getLatest(from: String, to: String): ExchangeRateEntity?

    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :from AND toCurrency = :to AND :date BETWEEN validFrom AND COALESCE(validTo, :date) LIMIT 1")
    suspend fun getAtDate(from: String, to: String, date: Long): ExchangeRateEntity?

    @Query("SELECT * FROM exchange_rates WHERE validTo IS NULL ORDER BY validFrom DESC")
    fun getAllActive(): Flow<List<ExchangeRateEntity>>
}