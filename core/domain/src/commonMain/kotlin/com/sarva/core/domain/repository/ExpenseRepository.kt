package com.sarva.core.domain.repository

import com.sarva.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>

    suspend fun getExpense(id: Int): Expense

    suspend fun insertExpense(expense: Expense)

    suspend fun deleteExpense(id: Int)
}