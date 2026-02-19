package com.sarva.features.location.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.util.Result
import com.sarva.features.location.domain.usecase.LocationSearchUseCase
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
class LocationSearchViewModel(
    private val locationSearchUseCase: LocationSearchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LocationSearchState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<LocationSearchEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            snapshotFlow { state.value.searchTextFieldState.text.toString() }
                .distinctUntilChanged()
                .debounce(300)
                .collectLatest { text ->
                    if (text.length >= 3) {
                        getSearchResults(text)
                    } else if (text.isEmpty()) {
                        _state.update { it.copy(results = emptyList(), isSearching = false) }
                    }
                }
        }
    }

    private suspend fun getSearchResults(query: String) {
        _state.update {
            it.copy(
                isSearching = true
            )
        }
        when (val result = locationSearchUseCase(query)) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        results = result.data,
                        isSearching = false
                    )
                }
            }

            is Result.Failure -> {
                _state.update {
                    it.copy(
                        results = emptyList(),
                        isSearching = false
                    )
                }
            }
        }
    }

    fun onAction(action: LocationSearchAction) {
        when (action) {
            is LocationSearchAction.OnLocationSelected -> {
                viewModelScope.launch {
                    eventChannel.send(LocationSearchEvent.LocationSelected(action.location))
                }
            }
        }
    }
}