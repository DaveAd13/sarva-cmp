package com.sarva.expenses.domain.usecase

import com.sarva.common.DispatcherProvider
import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.repository.ExpenseRepository
import com.sarva.core.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetExpensesUseCase(
    private val repository: ExpenseRepository,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Resource<List<Expense>>> = repository.getExpenses()
        .map { expenses ->
            Resource.Success(expenses) as Resource<List<Expense>>
        }
        .onStart {
            emit(Resource.Loading)
            delay(350)
        }
        .catch { e ->
            emit(Resource.Failure(e))
        }
        .flowOn(dispatchers.io)
}