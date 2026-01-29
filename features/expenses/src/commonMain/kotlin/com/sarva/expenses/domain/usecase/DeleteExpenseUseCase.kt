package com.sarva.expenses.domain.usecase

import com.sarva.core.domain.repository.ExpenseRepository
import com.sarva.core.domain.util.Result
import kotlin.coroutines.cancellation.CancellationException

class DeleteExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return try {
            repository.deleteExpense(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Failure(e)
        }
    }
}