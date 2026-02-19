package com.sarva.core.data.currencies.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentCurrencyLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val RECENT_CURRENCIES_KEY = stringPreferencesKey("recent_currencies")
    }

    val recentCurrencyCodes: Flow<List<String>> = dataStore.data.map { prefs ->
        prefs[RECENT_CURRENCIES_KEY]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    }

    suspend fun saveCurrency(code: String) {
        dataStore.edit { prefs ->
            val current = prefs[RECENT_CURRENCIES_KEY]?.split(",")?.filter { it.isNotBlank() }?.toMutableList()
                ?: mutableListOf()
            current.remove(code)
            current.add(0, code)
            prefs[RECENT_CURRENCIES_KEY] = current.take(3).joinToString(",")
        }
    }
}