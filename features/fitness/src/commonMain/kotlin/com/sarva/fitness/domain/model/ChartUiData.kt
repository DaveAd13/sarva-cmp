package com.sarva.fitness.domain.model

import androidx.compose.runtime.Immutable

enum class ChartTransition {
    FORWARD, BACKWARD, DEFAULT
}

@Immutable
data class ChartUiData(
    val bars: List<BarItem>,
    val maxRange: Float,
    val transition: ChartTransition = ChartTransition.DEFAULT
)