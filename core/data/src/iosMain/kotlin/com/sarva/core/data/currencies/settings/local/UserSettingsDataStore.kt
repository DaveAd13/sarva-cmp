package com.sarva.core.data.currencies.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sarva.core.data.common.local.datastore.createDataStore
import com.sarva.core.data.settings.local.USER_SETTINGS_FILE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createUserSettingsDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$USER_SETTINGS_FILE_NAME"
    }
)