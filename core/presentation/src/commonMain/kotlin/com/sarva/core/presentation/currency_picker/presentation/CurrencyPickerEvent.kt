package com.sarva.core.presentation.currency_picker.presentation

import com.sarva.core.domain.model.currency.Currency

sealed interface CurrencyPickerEvent {
    data class CurrencySelected(val currency: Currency) : CurrencyPickerEvent
}