package com.sarva.fitness.di

import com.sarva.fitness.data.repository.AndroidFitnessRepository
import com.sarva.fitness.domain.repository.FitnessRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformFitnessModule = module {
    //    singleOf(::AndroidFitnessRepository) bind FitnessRepository::class
    factoryOf(::AndroidFitnessRepository) bind FitnessRepository::class
}