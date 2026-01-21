package com.sarva.fitness.di

import com.sarva.fitness.data.repository.IosFitnessRepository
import com.sarva.fitness.domain.repository.FitnessRepository
import org.koin.dsl.module

actual val platformFitnessModule = module {
    single<FitnessRepository> { IosFitnessRepository() }
}