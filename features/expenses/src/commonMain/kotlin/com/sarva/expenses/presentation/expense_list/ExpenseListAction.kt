package com.sarva.expenses.presentation.expense_list

import com.sarva.core.domain.model.ExpenseCategory

sealed interface ExpenseListAction {
    data class CategoryClicked(val category: ExpenseCategory?) : ExpenseListAction
    data class ExpenseClicked(val expenseId: Int) : ExpenseListAction
}