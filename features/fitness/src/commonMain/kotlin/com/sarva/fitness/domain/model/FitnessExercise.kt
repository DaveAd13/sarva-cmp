package com.sarva.fitness.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.LocalDateTime

@Immutable
data class FitnessExercise(
    val id: String,
    val type: String,
    val icon: ImageVector,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val durationSeconds: Int,
)