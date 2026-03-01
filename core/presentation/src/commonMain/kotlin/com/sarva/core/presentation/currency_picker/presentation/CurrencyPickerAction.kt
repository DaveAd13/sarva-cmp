package com.sarva.core.presentation.currency_picker.presentation

import com.sarva.core.domain.currencies.model.Currency

sealed interface CurrencyPickerAction {
    data class OnCurrencySelected(val currency: Currency) : CurrencyPickerAction
}