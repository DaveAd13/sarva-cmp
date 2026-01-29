package com.sarva.expenses.presentation.expense_add_edit

import com.sarva.core.presentation.util.UiText

sealed interface ExpenseAddEditEvent {
    data object ExpenseSaved : ExpenseAddEditEvent
    data object ExpenseUpdated : ExpenseAddEditEvent
    data class ShowSnackbar(val message: UiText) : ExpenseAddEditEvent
}