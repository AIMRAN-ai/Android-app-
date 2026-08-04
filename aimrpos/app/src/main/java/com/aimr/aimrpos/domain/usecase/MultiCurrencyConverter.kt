package com.aimr.aimrpos.domain.usecase

import com.aimr.aimrpos.domain.model.Currency
import com.aimr.aimrpos.domain.model.ExchangeRate
import com.aimr.aimrpos.domain.repository.CurrencyRepository
import com.aimr.aimrpos.domain.repository.ExchangeRateRepository

class MultiCurrencyConverter(
    private val currencyRepository: CurrencyRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) {
    suspend fun convert(amount: Double, fromCurrency: String, toCurrency: String): Double {
        if (fromCurrency == toCurrency) return amount

        val fromRate = currencyRepository.getByCode(fromCurrency)?.exchangeRate ?: 1.0
        val toRate = currencyRepository.getByCode(toCurrency)?.exchangeRate ?: 1.0

        val rate = exchangeRateRepository.getLatest(fromCurrency, toCurrency)?.rate
            ?: exchangeRateRepository.getAtDate(fromCurrency, toCurrency, System.currentTimeMillis())?.rate
            ?: (toRate / fromRate)

        return ConvertCurrencyUseCase().invoke(amount, fromRate, toRate)
    }

    suspend fun convertWithBase(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val baseCurrency = currencyRepository.getBaseCurrency() ?: return amount
        if (fromCurrency == toCurrency) return amount

        val fromToBase = convert(amount, fromCurrency, baseCurrency.code)
        return convert(fromToBase, baseCurrency.code, toCurrency)
    }

    suspend fun formatAmount(amount: Double, currencyCode: String): String {
        val currency = currencyRepository.getByCode(currencyCode)
        val symbol = currency?.symbol ?: currencyCode
        return "$symbol ${"%.2f".format(amount)}"
    }
}