package com.sarva.fitness.di

import com.sarva.fitness.domain.usecase.CheckHealthPermissionsUseCase
import com.sarva.fitness.domain.usecase.GetActivityHistoryUseCase
import com.sarva.fitness.domain.usecase.GetDailyRecordsUseCase
import com.sarva.fitness.domain.usecase.GetExercisesUseCase
import com.sarva.fitness.presentation.activity_history.ActivityHistoryViewModel
import com.sarva.fitness.presentation.daily_activity.DailyActivityViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformFitnessModule: Module

val fitnessModule = module {

    factoryOf(::CheckHealthPermissionsUseCase)
    factoryOf(::GetActivityHistoryUseCase)
    factoryOf(::GetDailyRecordsUseCase)
    factoryOf(::GetExercisesUseCase)

    factory { GetDailyRecordsUseCase(get()) }
    factory { GetExercisesUseCase(get()) }

    viewModelOf(::DailyActivityViewModel)
    viewModelOf(::ActivityHistoryViewModel)
}