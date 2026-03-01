package com.sarva.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.sarva.core.data.currencies.local.createRecentCurrenciesDataStore
import com.sarva.core.data.database.AppDatabase
import com.sarva.core.data.settings.local.createUserSettingsDataStore
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformDataModule = module {
    single<AppDatabase> {
        val context = get<Context>()
        val dbFile = context.getDatabasePath("sarva.db")

        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single(named("currency_ds")) {
        createRecentCurrenciesDataStore(androidContext())
    }

    single(named("settings_ds")) {
        createUserSettingsDataStore(androidContext())
    }
}