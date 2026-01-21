package com.sarva.app.di

import com.sarva.core.data.di.dataModule
import com.sarva.core.data.di.platformDataModule
import com.sarva.expenses.di.expensesModule
import com.sarva.fitness.di.fitnessModule
import com.sarva.fitness.di.platformFitnessModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            fitnessModule,
            platformFitnessModule,
            expensesModule,
            dataModule,
            platformDataModule,
        )
    }
}