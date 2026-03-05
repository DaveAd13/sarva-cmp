package com.sarva.core.data.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.sarva.core.domain.settings.model.ThemeConfig
import com.sarva.core.domain.settings.model.UserSettings
import com.sarva.core.domain.settings.model.WidgetLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

class UserSettingsDataSource(
    private val dataStore: DataStore<Preferences>
) {

    fun getUserSettings(): Flow<UserSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            UserSettings(
                preferredCurrency = prefs[UserSettingsKeys.PREFERRED_CURRENCY] ?: "USD",
                stepGoal = prefs[UserSettingsKeys.STEP_GOAL] ?: 10000,
                homeLayout = WidgetLayout.valueOf(
                    prefs[UserSettingsKeys.HOME_LAYOUT] ?: WidgetLayout.TILED.name
                ),
                themeConfig = ThemeConfig.valueOf(
                    prefs[UserSettingsKeys.THEME_CONFIG] ?: ThemeConfig.SYSTEM.name
                )
            )
        }

    suspend fun updateCurrency(currencyCode: String) {
        dataStore.edit { it[UserSettingsKeys.PREFERRED_CURRENCY] = currencyCode }
    }

    suspend fun updateStepGoal(goal: Int) {
        dataStore.edit { it[UserSettingsKeys.STEP_GOAL] = goal }
    }

    suspend fun updateHomeLayout(layout: WidgetLayout) {
        dataStore.edit { it[UserSettingsKeys.HOME_LAYOUT] = layout.name }
    }

    suspend fun updateThemeConfig(config: ThemeConfig) {
        dataStore.edit { it[UserSettingsKeys.THEME_CONFIG] = config.name }
    }
}