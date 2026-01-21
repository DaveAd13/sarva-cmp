package com.sarva.expenses.domain.usecase

import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.domain.repository.ExpenseRepository
import com.sarva.core.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetGroupedExpensesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(category: ExpenseCategory?): Flow<Resource<Map<String, List<Expense>>>> {
        return repository.getExpenses()
            .map { list ->
                val filtered =
                    if (category == null) list else list.filter { it.category == category }
                val grouped = filtered
                    .sortedByDescending { it.dateTime }
                    .groupBy { expense ->
                        val date = expense.dateTime
                        "${
                            date.month.name.lowercase().replaceFirstChar { it.uppercase() }
                        } ${date.year}"
                    }
                Resource.Success(grouped) as Resource<Map<String, List<Expense>>>
            }
            .onStart { emit(Resource.Loading) }
            .catch { emit(Resource.Failure(it)) }
            .flowOn(Dispatchers.Default)
    }
}