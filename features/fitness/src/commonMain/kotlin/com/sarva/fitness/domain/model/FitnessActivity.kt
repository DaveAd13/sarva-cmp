package com.sarva.fitness.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class FitnessActivity(
    val records: List<FitnessRecords>,
    val exercises: List<FitnessExercise>
)
