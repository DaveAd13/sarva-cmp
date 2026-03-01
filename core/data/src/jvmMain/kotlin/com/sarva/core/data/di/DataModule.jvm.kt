package com.sarva.core.data.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.sarva.core.data.currencies.local.createRecentCurrenciesDataStore
import com.sarva.core.data.database.AppDatabase
import com.sarva.core.data.settings.local.createUserSettingsDataStore
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

actual val platformDataModule = module {
    single<AppDatabase> {
        val dbFile = File(System.getProperty("user.home"), ".sarva/sarva.db")

        if (!dbFile.parentFile.exists()) {
            dbFile.parentFile.mkdirs()
        }

        Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
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