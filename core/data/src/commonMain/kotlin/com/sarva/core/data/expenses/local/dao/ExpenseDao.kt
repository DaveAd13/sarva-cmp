package com.sarva.core.data.expenses.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sarva.core.data.expenses.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Suppress("AndroidUnresolvedRoomSqlReference")
@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY dateTime DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Int)

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Int): Flow<ExpenseEntity?>

}