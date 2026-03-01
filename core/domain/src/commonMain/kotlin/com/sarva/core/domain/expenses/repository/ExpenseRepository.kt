package com.sarva.core.domain.expenses.repository

import com.sarva.core.domain.expenses.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>

    fun getExpense(id: Int): Flow<Expense?>

    suspend fun insertExpense(expense: Expense)

    suspend fun deleteExpense(id: Int)
}