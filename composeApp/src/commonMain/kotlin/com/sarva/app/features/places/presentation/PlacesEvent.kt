package com.sarva.app.features.places.presentation

sealed interface PlacesEvent {
    data object NavigateTo : PlacesEvent
}