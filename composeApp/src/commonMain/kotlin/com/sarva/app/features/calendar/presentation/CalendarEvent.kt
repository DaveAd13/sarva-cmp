package com.sarva.app.features.calendar.presentation

sealed interface CalendarEvent {
    data object NavigateTo : CalendarEvent
}