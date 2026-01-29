package com.sarva.expenses.presentation.expense_details

sealed interface ExpenseDetailsAction {
    data object OnEditClicked : ExpenseDetailsAction
    data object OnDeleteClicked : ExpenseDetailsAction
    data object OnDeleteConfirmed : ExpenseDetailsAction
    data object OnDeleteCancelled : ExpenseDetailsAction
}