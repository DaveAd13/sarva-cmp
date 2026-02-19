package com.sarva.core.presentation.di

import com.sarva.core.domain.usecase.GetCurrenciesUseCase
import com.sarva.core.domain.usecase.GetRecentCurrenciesUseCase
import com.sarva.core.domain.usecase.SaveCurrencyToRecentsUseCase
import com.sarva.core.presentation.currency_picker.presentation.CurrencyPickerViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    factoryOf(::GetCurrenciesUseCase)
    factoryOf(::GetRecentCurrenciesUseCase)
    factoryOf(::SaveCurrencyToRecentsUseCase)
    viewModelOf(::CurrencyPickerViewModel)
}