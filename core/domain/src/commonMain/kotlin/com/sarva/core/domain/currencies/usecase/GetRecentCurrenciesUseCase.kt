package com.sarva.core.domain.currencies.usecase

import com.sarva.core.domain.currencies.model.Currency
import com.sarva.core.domain.currencies.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class GetRecentCurrenciesUseCase(
    private val repository: CurrencyRepository
) {
    operator fun invoke(): Flow<List<Currency>> {
        return repository.getRecentCurrencies()
            .catch { e ->
                emit(emptyList())
            }
    }
}