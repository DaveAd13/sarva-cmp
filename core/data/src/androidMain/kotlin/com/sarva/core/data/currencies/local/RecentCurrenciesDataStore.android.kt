package com.sarva.core.data.currencies.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sarva.core.data.common.local.datastore.createDataStore

fun createRecentCurrenciesDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(CURRENCY_DATA_STORE_FILE_NAME).absolutePath }
)