package com.sarva.core.presentation.currency_picker.presentation

import com.sarva.core.domain.currencies.model.Currency

sealed interface CurrencyPickerEvent {
    data class CurrencySelected(val currency: Currency) : CurrencyPickerEvent
}