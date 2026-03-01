package com.sarva.core.data.settings.repository

import com.sarva.core.data.settings.local.UserSettingsDataSource
import com.sarva.core.domain.settings.model.ThemeConfig
import com.sarva.core.domain.settings.model.UserSettings
import com.sarva.core.domain.settings.model.WidgetLayout
import com.sarva.core.domain.settings.repository.UserSettingsRepository
import kotlinx.coroutines.flow.first

class UserSettingsRepositoryImpl(
    private val dataSource: UserSettingsDataSource
) : UserSettingsRepository {

    override suspend fun getUserSettings(): UserSettings {
        return dataSource.getUserSettings().first()
    }

    override suspend fun updateCurrency(currencyCode: String) {
        dataSource.updateCurrency(currencyCode)
    }

    override suspend fun updateStepGoal(goal: Int) {
        dataSource.updateStepGoal(goal)
    }

    override suspend fun updateHomeLayout(layout: WidgetLayout) {
        dataSource.updateHomeLayout(layout)
    }

    override suspend fun updateThemeConfig(config: ThemeConfig) {
        dataSource.updateThemeConfig(config)
    }
}
