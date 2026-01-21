package com.sarva.fitness.domain.model

import androidx.compose.runtime.Stable

enum class ChartTransition {
    FORWARD, BACKWARD, DEFAULT
}

@Stable
data class ChartUiData(
    val bars: List<BarItem>,
    val maxRange: Float,
    val transition: ChartTransition = ChartTransition.DEFAULT
)