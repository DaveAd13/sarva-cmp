package com.sarva.app.features.more.presentation

sealed interface MoreEvent {
    data object NavigateTo : MoreEvent
}