package com.sarva.expenses.di

import com.sarva.expenses.domain.usecase.DeleteExpenseUseCase
import com.sarva.expenses.domain.usecase.GetExpensesUseCase
import com.sarva.expenses.domain.usecase.GetGroupedExpensesUseCase
import com.sarva.expenses.domain.usecase.InsertExpenseUseCase
import com.sarva.expenses.presentation.expense_add_edit.ExpenseAddEditViewModel
import com.sarva.expenses.presentation.expense_details.ExpenseDetailsViewModel
import com.sarva.expenses.presentation.expense_list.ExpenseListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val expensesModule = module {

    factoryOf(::GetExpensesUseCase)
    factoryOf(::InsertExpenseUseCase)
    factoryOf(::DeleteExpenseUseCase)
    factoryOf(::GetGroupedExpensesUseCase)

    viewModelOf(::ExpenseListViewModel)
//    viewModelOf(::ExpenseAddEditViewModel)
    viewModel { params ->
        ExpenseAddEditViewModel(
            expenseId = params.get()
        )
    }
    viewModelOf(::ExpenseDetailsViewModel)
}