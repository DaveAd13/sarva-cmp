package com.sarva.expenses.presentation.expense_list

import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.model.expense.ExpenseCategory

sealed interface ExpenseListAction {
    data class CategoryClicked(val category: ExpenseCategory?) : ExpenseListAction
    data object ToggleSearch : ExpenseListAction
    data class DeleteExpense(val expense: Expense) : ExpenseListAction
    data class UndoDelete(val expense: Expense) : ExpenseListAction
}