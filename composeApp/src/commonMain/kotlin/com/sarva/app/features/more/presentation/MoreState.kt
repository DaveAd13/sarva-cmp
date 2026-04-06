package com.sarva.app.features.more.presentation

import com.sarva.core.domain.settings.model.ThemeConfig

data class MoreState(
    val isLoading: Boolean = false,
    val theme: ThemeConfig = ThemeConfig.SYSTEM,
    val currency: String = "USD",
    val stepGoal: Int = 10000,
)