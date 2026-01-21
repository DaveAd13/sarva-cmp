package com.sarva.app.features.tasks.domain.model

data class Task (
    val id: String,
    val isCompleted: Boolean,
    val title: String
)