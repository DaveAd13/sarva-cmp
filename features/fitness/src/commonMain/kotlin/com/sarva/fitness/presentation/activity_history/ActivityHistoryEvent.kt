package com.sarva.fitness.presentation.activity_history

sealed interface ActivityHistoryEvent {
    data object NavigateTo : ActivityHistoryEvent
}