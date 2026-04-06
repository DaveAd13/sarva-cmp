package com.sarva.core.data.di

import com.sarva.common.DefaultDispatcherProvider
import com.sarva.common.DispatcherProvider
import com.sarva.core.data.currencies.local.RecentCurrenciesDataSource
import com.sarva.core.data.currencies.repository.CurrencyRepositoryImpl
import com.sarva.core.data.expenses.repository.ExpenseRepositoryImpl
import com.sarva.core.data.location.remote.LocationApi
import com.sarva.core.data.location.repository.LocationRepositoryImpl
import com.sarva.core.data.settings.local.UserSettingsDataSource
import com.sarva.core.data.settings.repository.UserSettingsRepositoryImpl
import com.sarva.core.domain.currencies.repository.CurrencyRepository
import com.sarva.core.domain.expenses.repository.ExpenseRepository
import com.sarva.core.domain.location.repository.LocationRepository
import com.sarva.core.domain.settings.repository.UserSettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataModule: Module

val dataModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }
    singleOf(::LocationApi)
    singleOf(::ExpenseRepositoryImpl) bind ExpenseRepository::class
    singleOf(::LocationRepositoryImpl) bind LocationRepository::class
    singleOf(::CurrencyRepositoryImpl) bind CurrencyRepository::class
    singleOf(::UserSettingsRepositoryImpl) bind UserSettingsRepository::class
    single {
        RecentCurrenciesDataSource(get(named("currency_ds")))
    }
    single {
        UserSettingsDataSource(get(named("settings_ds")))
    }
}