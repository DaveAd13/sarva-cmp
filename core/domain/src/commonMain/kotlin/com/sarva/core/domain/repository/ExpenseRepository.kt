package com.sarva.core.domain.repository

import com.sarva.core.domain.model.expense.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>

    fun getExpense(id: Int): Flow<Expense?>

    suspend fun insertExpense(expense: Expense)

    suspend fun deleteExpense(id: Int)
}