package com.sarva.core.data.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sarva.core.data.common.local.datastore.createDataStore
import java.io.File

fun createUserSettingsDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(System.getProperty("java.io.tmpdir"), USER_SETTINGS_FILE_NAME)
        file.absolutePath
    }
)