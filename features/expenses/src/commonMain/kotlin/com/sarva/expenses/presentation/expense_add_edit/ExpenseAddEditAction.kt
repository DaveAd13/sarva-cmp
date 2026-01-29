package com.sarva.expenses.presentation.expense_add_edit

import com.sarva.core.domain.model.expense.ExpenseCategory
import com.sarva.core.domain.model.location.LocationSearchResult
import kotlinx.datetime.LocalDateTime

sealed interface ExpenseAddEditAction {
    data object OnSaveClicked : ExpenseAddEditAction
    data class OnCategoryClicked(val category: ExpenseCategory) : ExpenseAddEditAction
    data object OnDateClicked : ExpenseAddEditAction
    data object OnLocationCLicked : ExpenseAddEditAction
    data object OnDatePickerDismissed : ExpenseAddEditAction
    data object OnLocationSearchDismissed : ExpenseAddEditAction
    data class OnDateSelected(val dateTime: LocalDateTime) : ExpenseAddEditAction
    data class OnLocationSelected(val location: LocationSearchResult) : ExpenseAddEditAction
    data object OnEntryChanged : ExpenseAddEditAction
    data class OnEntryRemoved(val index: Int) : ExpenseAddEditAction
}