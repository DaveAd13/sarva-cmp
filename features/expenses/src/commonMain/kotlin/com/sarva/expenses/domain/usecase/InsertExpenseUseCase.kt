package com.sarva.expenses.domain.usecase

import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.repository.ExpenseRepository
import com.sarva.core.domain.util.Result

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