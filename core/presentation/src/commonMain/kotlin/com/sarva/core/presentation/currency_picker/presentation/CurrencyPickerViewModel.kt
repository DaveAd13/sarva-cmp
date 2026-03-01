package com.sarva.core.presentation.currency_picker.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.currencies.usecase.GetCurrenciesUseCase
import com.sarva.core.domain.currencies.usecase.GetRecentCurrenciesUseCase
import com.sarva.core.domain.currencies.usecase.SaveCurrencyToRecentsUseCase
import com.sarva.core.domain.util.Result
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class CurrencyPickerViewModel(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getRecentCurrenciesUseCase: GetRecentCurrenciesUseCase,
    private val saveCurrencyToRecentsUseCase: SaveCurrencyToRecentsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CurrencyPickerState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CurrencyPickerEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getRecentCurrenciesUseCase().collect { recents ->
                _state.update {
                    it.copy(
                        recentCurrencies = recents
                    )
                }
            }
        }

        viewModelScope.launch {
            getCurrencies("")
            snapshotFlow { state.value.searchTextFieldState.text.toString() }
                .distinctUntilChanged()
                .debounce(100)
                .collectLatest { text ->
                    getCurrencies(text)
                }
        }
    }

    private suspend fun getCurrencies(query: String) {
        _state.update {
            it.copy(
                isSearching = true
            )
        }
        when (val result = getCurrenciesUseCase(query)) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        currencies = result.data,
                        isSearching = false
                    )
                }
            }

            is Result.Failure -> {
                _state.update {
                    it.copy(
                        currencies = emptyList(),
                        isSearching = false
                    )
                }
            }
        }
    }

    fun onAction(action: CurrencyPickerAction) {
        when (action) {
            is CurrencyPickerAction.OnCurrencySelected -> {
                viewModelScope.launch {
                    saveCurrencyToRecentsUseCase(action.currency.code)
                    eventChannel.send(CurrencyPickerEvent.CurrencySelected(action.currency))
                }
            }
        }
    }
}