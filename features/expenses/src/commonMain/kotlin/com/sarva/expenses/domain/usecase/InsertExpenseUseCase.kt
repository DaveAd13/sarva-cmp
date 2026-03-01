package com.sarva.expenses.domain.usecase

import com.sarva.core.domain.expenses.model.Expense
import com.sarva.core.domain.expenses.repository.ExpenseRepository
import com.sarva.core.domain.util.Result
import kotlin.coroutines.cancellation.CancellationException

class InsertExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Result<Unit> {
        return try {
            repository.insertExpense(expense)
            Result.Success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Failure(e)
        }
    }
}