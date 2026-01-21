package com.sarva.expenses.domain.usecase

import com.sarva.core.domain.util.Result
import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.repository.ExpenseRepository

class InsertExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Result<Unit> {
        return try {
            repository.insertExpense(expense)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}