package com.sarva.features.location.presentation

import com.sarva.core.domain.model.location.LocationSearchResult

sealed interface LocationSearchEvent {
    data class LocationSelected(val location: LocationSearchResult) : LocationSearchEvent
}