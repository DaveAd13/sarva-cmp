package com.sarva.features.location.presentation

import com.sarva.core.domain.model.location.LocationSearchResult

sealed interface LocationSearchAction {
    data class OnLocationSelected(val location: LocationSearchResult) : LocationSearchAction
}