package com.sarva.core.domain.usecase

import com.sarva.core.domain.repository.CurrencyRepository

class SaveCurrencyToRecentsUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(currencyCode: String) =
        repository.saveCurrencyToRecents(currencyCode)
}