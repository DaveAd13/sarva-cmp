package com.sarva.app.features.home.domain.model

import androidx.compose.runtime.Stable

@Stable
data class SpentInfo(
    val totalSpent: Double = 0.0,
    val currency: String = "$",
    val recentSpendingTrend: List<Float> = emptyList()
)