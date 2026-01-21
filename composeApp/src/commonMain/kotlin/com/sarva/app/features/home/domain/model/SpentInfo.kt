package com.sarva.app.features.home.domain.model

data class SpentInfo(
    val totalSpent: Double = 0.0,
    val currency: String = "$",
    val recentSpendingTrend: List<Float> = emptyList()
)