package com.sarva.fitness.presentation.daily_activity

sealed interface DailyActivityEvent {
    data object NavigateTo : DailyActivityEvent
}