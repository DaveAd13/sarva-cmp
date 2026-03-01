package com.sarva.core.data.expenses.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sarva.core.domain.expenses.model.ExpenseCategory
import com.sarva.core.domain.expenses.model.ExpenseEntry
import com.sarva.core.domain.expenses.model.ExpenseLocation
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: ExpenseCategory,
    val amount: Double,
    val currency: String,
    val dateTime: LocalDateTime,

    @Embedded(prefix = "loc_")
    val location: ExpenseLocation?,

    val entries: List<ExpenseEntry>,
)