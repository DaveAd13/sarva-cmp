package com.sarva.core.data.repository

import com.sarva.core.data.database.AppDatabase
import com.sarva.core.data.mappers.toDomain
import com.sarva.core.data.mappers.toEntity
import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val database: AppDatabase
) : ExpenseRepository {

    private val expenseDao by lazy { database.expenseDao() }

    override fun getExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
            .flowOn(Dispatchers.IO)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun getExpenseById(id: Int): Expense {
        return withContext(Dispatchers.IO) {
            expenseDao.getExpenseById(id).toDomain()
        }
    }

    override suspend fun insertExpense(expense: Expense) {
        withContext(Dispatchers.IO) {
            expenseDao.insertExpense(expense.toEntity())
        }
    }

    override suspend fun deleteExpense(id: Int) {
        withContext(Dispatchers.IO) {
            expenseDao.deleteExpenseById(id)
        }
    }
}