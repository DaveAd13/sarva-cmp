package com.sarva.core.data.settings.local

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserSettingsKeys {
    val PREFERRED_CURRENCY = stringPreferencesKey("preferred_currency")
    val STEP_GOAL = intPreferencesKey("step_goal")
    val HOME_LAYOUT = stringPreferencesKey("home_layout")
    val THEME_CONFIG = stringPreferencesKey("theme_config")
}

internal const val USER_SETTINGS_FILE_NAME = "user_settings.preferences_pb"