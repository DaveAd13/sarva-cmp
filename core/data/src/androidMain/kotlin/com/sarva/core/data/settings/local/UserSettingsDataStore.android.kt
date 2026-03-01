package com.sarva.core.data.settings.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sarva.core.data.common.local.datastore.createDataStore

fun createUserSettingsDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(USER_SETTINGS_FILE_NAME).absolutePath }
)