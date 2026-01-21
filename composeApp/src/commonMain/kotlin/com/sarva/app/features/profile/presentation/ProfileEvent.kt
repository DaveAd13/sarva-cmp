package com.sarva.app.features.profile.presentation

sealed interface ProfileEvent {
    data object NavigateTo : ProfileEvent
}