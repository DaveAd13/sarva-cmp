package com.sarva.core.data.currencies.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sarva.core.data.common.local.datastore.createDataStore
import java.io.File

fun createRecentCurrenciesDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(System.getProperty("java.io.tmpdir"), CURRENCY_DATA_STORE_FILE_NAME)
        file.absolutePath
    }
)