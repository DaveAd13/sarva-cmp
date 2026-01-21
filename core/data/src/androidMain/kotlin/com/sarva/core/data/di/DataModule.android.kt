package com.sarva.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.sarva.core.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
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
}