package com.sarva.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.sarva.core.data.database.expenses.ExpenseDao
import com.sarva.core.data.database.expenses.ExpenseEntity


@Database(entities = [ExpenseEntity::class], version = 5)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}