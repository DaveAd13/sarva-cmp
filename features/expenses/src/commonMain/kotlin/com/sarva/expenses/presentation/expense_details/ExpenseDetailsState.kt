package com.sarva.expenses.presentation.expense_details

import androidx.compose.runtime.Immutable
import com.sarva.core.domain.model.expense.Expense

@Immutable
data class ExpenseDetailsState(
    val isLoading: Boolean = false,
    val expense: Expense? = null,
    val showDeleteDialog: Boolean = false
)