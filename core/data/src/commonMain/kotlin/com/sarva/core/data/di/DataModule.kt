package com.sarva.core.data.di

import com.sarva.core.data.repository.ExpenseRepositoryImpl
import com.sarva.core.domain.repository.ExpenseRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataModule: Module

val dataModule = module {
    factoryOf(::ExpenseRepositoryImpl) bind ExpenseRepository::class

}