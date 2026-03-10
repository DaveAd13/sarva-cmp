package com.sarva.core.domain.settings.repository

import com.sarva.core.domain.settings.model.ThemeConfig
import com.sarva.core.domain.settings.model.UserSettings
import com.sarva.core.domain.settings.model.WidgetLayout
import kotlinx.coroutines.flow.StateFlow

interface UserSettingsRepository {

    val userSettings: StateFlow<UserSettings>
    suspend fun getUserSettings(): UserSettings

    suspend fun updateCurrency(currencyCode: String)

    suspend fun updateStepGoal(goal: Int)

    suspend fun updateHomeLayout(layout: WidgetLayout)

    suspend fun updateThemeConfig(config: ThemeConfig)
}