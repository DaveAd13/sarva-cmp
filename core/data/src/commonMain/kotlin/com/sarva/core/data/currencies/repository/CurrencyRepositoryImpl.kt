package com.sarva.core.data.currencies.repository

import com.sarva.core.data.currencies.dto.CurrencyDto
import com.sarva.core.data.currencies.local.RecentCurrencyLocalDataSource
import com.sarva.core.data.currencies.mapper.toDomain
import com.sarva.core.data.generated.resources.Res
import com.sarva.core.domain.model.currency.Currency
import com.sarva.core.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class CurrencyRepositoryImpl(
    private val localDataSource: RecentCurrencyLocalDataSource,
) : CurrencyRepository {

    private var cachedCurrencies: List<Currency>? = null

    override suspend fun getCurrencies(): List<Currency> {
        return cachedCurrencies ?: loadCurrenciesFromDisk().also { cachedCurrencies = it }
    }

    private suspend fun loadCurrenciesFromDisk(): List<Currency> {
        return try {
            val bytes = Res.readBytes("files/currencies.json")
            val rawMap = Json.decodeFromString<Map<String, CurrencyDto>>(bytes.decodeToString())
            rawMap.values.map { it.toDomain() }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emptyList()
        }
    }

    override fun getRecentCurrencies(): Flow<List<Currency>> {
        return localDataSource.recentCurrencyCodes.map { codes ->
            val all = getCurrencies()
            codes.mapNotNull { code ->
                all.find { it.code == code }
            }
        }
    }

    override suspend fun saveCurrencyToRecents(code: String) {
        localDataSource.saveCurrency(code)
    }
}