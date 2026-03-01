package com.sarva.core.domain.currencies.usecase

import com.sarva.core.domain.currencies.repository.CurrencyRepository

class SaveCurrencyToRecentsUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(currencyCode: String) =
        repository.saveCurrencyToRecents(currencyCode)
}