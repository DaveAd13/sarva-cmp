package com.sarva.expenses.presentation.expense_list

import com.sarva.core.presentation.util.UiText

sealed interface ExpenseListEvent {
    data class ShowSnackbar(val message: UiText) : ExpenseListEvent
}