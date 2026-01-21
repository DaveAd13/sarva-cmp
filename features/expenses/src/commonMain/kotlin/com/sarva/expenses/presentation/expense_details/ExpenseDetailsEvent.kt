package com.sarva.expenses.presentation.expense_details

sealed interface ExpenseDetailsEvent {
    data object NavigateTo : ExpenseDetailsEvent
}