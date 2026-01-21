package com.sarva.core.data.database

import androidx.room.TypeConverter
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.domain.model.ExpenseEntry
import com.sarva.core.domain.model.Participant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString)

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String = date.toString()

    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime = LocalDateTime.parse(dateString)

    @TypeConverter
    fun fromExpenseCategory(category: ExpenseCategory): String = category.name

    @TypeConverter
    fun toExpenseCategory(name: String): ExpenseCategory = ExpenseCategory.valueOf(name)

    // Converters for Lists using Kotlin Serialization
    @TypeConverter
    fun fromEntryList(value: List<ExpenseEntry>): String = json.encodeToString(value)

    @TypeConverter
    fun toEntryList(value: String): List<ExpenseEntry> = json.decodeFromString(value)

    @TypeConverter
    fun fromParticipantList(value: List<Participant>): String = json.encodeToString(value)

    @TypeConverter
    fun toParticipantList(value: String): List<Participant> = json.decodeFromString(value)
}