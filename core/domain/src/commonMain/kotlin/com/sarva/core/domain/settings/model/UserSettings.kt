package com.sarva.core.domain.settings.model

data class UserSettings(
    val preferredCurrency: String = "USD",
    val stepGoal: Int = 10000,
    val homeLayout: WidgetLayout = WidgetLayout.TILED,
    val themeConfig: ThemeConfig = ThemeConfig.SYSTEM
)