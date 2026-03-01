package com.sarva.app.features.home.presentation

sealed interface HomeEvent {
    object RequestHealthPermission : HomeEvent
}