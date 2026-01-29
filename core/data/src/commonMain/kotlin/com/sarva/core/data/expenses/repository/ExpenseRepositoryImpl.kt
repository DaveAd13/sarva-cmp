package com.sarva.core.data.expenses.repository

import com.sarva.common.DispatcherProvider
import com.sarva.core.data.database.AppDatabase
import com.sarva.core.data.expenses.mapper.toDomain
import com.sarva.core.data.expenses.mapper.toEntity
import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val database: AppDatabase,
    private val dispatchers: DispatcherProvider
) : ExpenseRepository {

    private val expenseDao by lazy { database.expenseDao() }

    override fun getExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
            .map { entities ->
                entities.map { it.toDomain() }
            }.flowOn(dispatchers.io)
    }

    override fun getExpense(id: Int): Flow<Expense?> {
        return expenseDao.getExpenseById(id)
            .map { it?.toDomain() }
            .flowOn(dispatchers.io)

    }

    override suspend fun insertExpense(expense: Expense) {
        withContext(dispatchers.io) {
            expenseDao.insertExpense(expense.toEntity())
        }
    }

    override suspend fun deleteExpense(id: Int) {
        withContext(dispatchers.io) {
            expenseDao.deleteExpenseById(id)
        }
    }
}