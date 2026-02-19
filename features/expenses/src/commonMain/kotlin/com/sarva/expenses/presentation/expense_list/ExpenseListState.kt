package com.sarva.expenses.presentation.expense_list

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.model.expense.ExpenseCategory

@Stable
data class ExpenseListState(
    val isLoading: Boolean = true,
    val isSearchActive: Boolean = false,
    val expenses: List<Expense> = emptyList(),
    val selectedCategory: ExpenseCategory? = null,
    val groupedExpenses: Map<String, List<Expense>> = emptyMap(),
    val searchTextFieldState: TextFieldState = TextFieldState(),
)