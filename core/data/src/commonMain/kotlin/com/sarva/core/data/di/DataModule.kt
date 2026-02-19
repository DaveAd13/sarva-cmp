package com.sarva.core.data.di

import com.sarva.common.DefaultDispatcherProvider
import com.sarva.common.DispatcherProvider
import com.sarva.core.data.currencies.local.RecentCurrencyLocalDataSource
import com.sarva.core.data.currencies.repository.CurrencyRepositoryImpl
import com.sarva.core.data.expenses.repository.ExpenseRepositoryImpl
import com.sarva.core.data.location.remote.LocationApi
import com.sarva.core.data.location.repository.LocationRepositoryImpl
import com.sarva.core.domain.repository.CurrencyRepository
import com.sarva.core.domain.repository.ExpenseRepository
import com.sarva.core.domain.repository.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataModule: Module

val dataModule = module {
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
    factoryOf(::ExpenseRepositoryImpl) bind ExpenseRepository::class
    factoryOf(::LocationRepositoryImpl) bind LocationRepository::class
    factoryOf(::CurrencyRepositoryImpl) bind CurrencyRepository::class
    singleOf(::RecentCurrencyLocalDataSource)
}