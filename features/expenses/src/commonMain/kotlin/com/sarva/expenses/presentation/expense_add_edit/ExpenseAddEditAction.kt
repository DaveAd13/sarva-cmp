package com.sarva.expenses.presentation.expense_add_edit

import com.sarva.core.domain.model.ExpenseCategory
import kotlinx.datetime.LocalDateTime

sealed interface ExpenseAddEditAction {
    data object OnSaveClicked : ExpenseAddEditAction
    data class OnCategoryClicked(val category: ExpenseCategory) : ExpenseAddEditAction
    data object OnDateClicked : ExpenseAddEditAction
    data object OnDatePickerDismissed : ExpenseAddEditAction
    data class OnDateSelected(val dateTime: LocalDateTime) : ExpenseAddEditAction
    data object OnEntryChanged : ExpenseAddEditAction
    data class OnEntryRemoved(val index: Int) : ExpenseAddEditAction
}