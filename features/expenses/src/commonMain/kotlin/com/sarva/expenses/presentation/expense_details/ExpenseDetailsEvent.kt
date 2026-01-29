package com.sarva.expenses.presentation.expense_details

import com.sarva.core.presentation.util.UiText

sealed interface ExpenseDetailsEvent {
    data class ShowSnackbar(val message: UiText) : ExpenseDetailsEvent
    data object ExpenseLoadingFailed : ExpenseDetailsEvent
    data object ExpenseDeleted : ExpenseDetailsEvent
    data object OnEditClicked: ExpenseDetailsEvent
}