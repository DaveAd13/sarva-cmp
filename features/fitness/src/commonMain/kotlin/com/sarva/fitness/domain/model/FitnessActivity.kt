package com.sarva.fitness.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class FitnessActivity(
    val records: List<FitnessRecords>,
    val exercises: List<FitnessExercise>
) {
    fun totalSteps() = records.sumOf { it.steps }
    fun totalCalories() = records.sumOf { it.calories }
    fun totalDistance() = records.sumOf { it.distance }
}
