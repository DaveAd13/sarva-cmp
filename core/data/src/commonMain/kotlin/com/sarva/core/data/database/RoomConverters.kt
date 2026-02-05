package com.sarva.core.data.database

import androidx.room.TypeConverter
import com.sarva.core.domain.model.expense.ExpenseCategory
import com.sarva.core.domain.model.expense.ExpenseEntry
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String = date.toString()

    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime = LocalDateTime.parse(dateString)

    @TypeConverter
    fun fromExpenseCategory(category: ExpenseCategory): String = category.name

    @TypeConverter
    fun toExpenseCategory(name: String): ExpenseCategory = ExpenseCategory.valueOf(name)

    @TypeConverter
    fun fromEntryList(value: List<ExpenseEntry>): String = json.encodeToString(value)

    @TypeConverter
    fun toEntryList(value: String): List<ExpenseEntry> = json.decodeFromString(value)
}