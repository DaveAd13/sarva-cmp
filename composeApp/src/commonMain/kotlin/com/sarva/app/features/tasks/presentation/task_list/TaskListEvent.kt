package com.sarva.app.features.tasks.presentation.task_list

sealed interface TaskListEvent {
    data object NavigateTo : TaskListEvent
}