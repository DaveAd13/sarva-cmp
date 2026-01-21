package com.sarva.core.data.database.expenses

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.domain.model.ExpenseEntry
import com.sarva.core.domain.model.GeoLocation
import com.sarva.core.domain.model.Participant
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
    val location: GeoLocation?,

    val breakdown: List<ExpenseEntry>,
    val participants: List<Participant>
)