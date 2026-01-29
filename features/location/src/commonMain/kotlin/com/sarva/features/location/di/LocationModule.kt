package com.sarva.features.location.di

import com.sarva.features.location.domain.usecase.LocationSearchUseCase
import com.sarva.features.location.presentation.LocationSearchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val locationModule = module {

    factoryOf(::LocationSearchUseCase)
    viewModelOf(::LocationSearchViewModel)
}