package com.sarva.core.presentation.currency_picker.presentation

import com.sarva.core.domain.model.currency.Currency

sealed interface CurrencyPickerAction {
    data class OnCurrencySelected(val currency: Currency) : CurrencyPickerAction
}