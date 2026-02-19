package com.sarva.core.domain.repository

import com.sarva.core.domain.model.currency.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getCurrencies(): List<Currency>
    fun getRecentCurrencies(): Flow<List<Currency>>
    suspend fun saveCurrencyToRecents(code: String)
}