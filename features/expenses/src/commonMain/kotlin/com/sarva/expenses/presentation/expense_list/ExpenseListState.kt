package com.sarva.expenses.presentation.expense_list

import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.model.ExpenseCategory


data class ExpenseListState(
    val isLoading: Boolean = true,
    val expenses: List<Expense> = emptyList(),
    val selectedCategory: ExpenseCategory? = null,
    val groupedExpenses: Map<String, List<Expense>> = emptyMap(),
)