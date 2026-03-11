package com.sarva.fitness.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChartUiData(
    val bars: List<BarItem>,
    val maxRange: Float,
    val showGoalLine: Boolean,
    val stepGoal: Int
)