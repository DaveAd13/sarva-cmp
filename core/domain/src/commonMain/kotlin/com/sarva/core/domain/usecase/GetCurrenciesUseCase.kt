package com.sarva.core.domain.usecase

import com.sarva.core.domain.model.currency.Currency
import com.sarva.core.domain.repository.CurrencyRepository
import com.sarva.core.domain.util.Result
import kotlin.coroutines.cancellation.CancellationException

class GetCurrenciesUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(query: String = ""): Result<List<Currency>> {
        return try {
            val allCurrencies = repository.getCurrencies()

            val filtered = allCurrencies.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.code.contains(query, ignoreCase = true)
            }

            Result.Success(filtered)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Failure(e)
        }
    }
}