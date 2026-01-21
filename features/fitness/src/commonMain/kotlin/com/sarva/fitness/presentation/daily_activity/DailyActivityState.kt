package com.sarva.fitness.presentation.daily_activity

import com.sarva.fitness.domain.model.FitnessExercise
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DailyActivityState(
    val isLoading: Boolean = false,
    val goal: Int = DailyActivityViewModel.STEP_GOAL,
    val steps: Int = 0,
    val calories: String = "0",
    val distance: String = "0",
    val progress: Float = 0f,
    val exercises: ImmutableList<FitnessExercise> = persistentListOf()
)