package com.sarva.features.location.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.sarva.core.domain.location.model.LocationSearchResult

data class LocationSearchState(
    val searchTextFieldState: TextFieldState = TextFieldState(""),
    val results: List<LocationSearchResult> = emptyList(),
    val isSearching: Boolean = false,
)