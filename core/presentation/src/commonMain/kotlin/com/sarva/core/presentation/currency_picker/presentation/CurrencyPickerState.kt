package com.sarva.core.presentation.currency_picker.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.sarva.core.domain.currencies.model.Currency

data class CurrencyPickerState(
    val searchTextFieldState: TextFieldState = TextFieldState(""),
    val currencies: List<Currency> = emptyList(),
    val recentCurrencies: List<Currency> = emptyList(),
    val isSearching: Boolean = false,
)