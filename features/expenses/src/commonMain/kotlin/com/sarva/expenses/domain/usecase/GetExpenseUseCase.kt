package com.sarva.expenses.domain.usecase

import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.repository.ExpenseRepository
import com.sarva.core.domain.util.Result

class GetExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(id: Int): Result<Expense> {
        return try {
            val expense = repository.getExpense(id)
            Result.Success(expense)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}