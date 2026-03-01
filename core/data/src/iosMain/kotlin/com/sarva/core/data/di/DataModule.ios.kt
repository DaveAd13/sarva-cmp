package com.sarva.core.data.di

import androidx.room.Room
import com.sarva.core.data.currencies.local.createRecentCurrenciesDataStore
import com.sarva.core.data.currencies.settings.local.createUserSettingsDataStore
import com.sarva.core.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual val platformDataModule = module {
    single<AppDatabase> {
        val dbFilePath = NSHomeDirectory() + "/sarva.db"

        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
            .setDriver(androidx.sqlite.driver.bundled.BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single(named("currency_ds")) {
        createRecentCurrenciesDataStore()
    }

    single(named("settings_ds")) {
        createUserSettingsDataStore()
    }
}