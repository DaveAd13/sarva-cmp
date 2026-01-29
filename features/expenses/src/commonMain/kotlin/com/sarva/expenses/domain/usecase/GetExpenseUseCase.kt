package com.sarva.expenses.domain.usecase

import com.sarva.common.DispatcherProvider
import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.repository.ExpenseRepository
import com.sarva.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetExpenseUseCase(
    private val repository: ExpenseRepository,
    private val dispatchers: DispatcherProvider
) {

    operator fun invoke(id: Int): Flow<Resource<Expense>> = repository.getExpense(id)
        .map { expense ->
            if (expense != null) {
                Resource.Success(expense)
            } else {
                Resource.Failure(NoSuchElementException())
            }
        }
        .onStart {
            emit(Resource.Loading)
        }
        .catch { e ->
            emit(Resource.Failure(e))
        }
        .flowOn(dispatchers.io)
}