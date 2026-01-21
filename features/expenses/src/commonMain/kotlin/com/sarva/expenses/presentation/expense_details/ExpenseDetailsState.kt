package com.sarva.expenses.presentation.expense_details

import com.sarva.core.domain.model.Expense

data class ExpenseDetailsState(
    val expense: Expense? = null
)