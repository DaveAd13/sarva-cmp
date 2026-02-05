package com.sarva.core.domain.model.expense

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    val id: Int = 0,
    val title: String,
    val category: ExpenseCategory,
    val amount: Double,
    val currency: String,
    val dateTime: LocalDateTime,
    val location: ExpenseLocation? = null,
    val entries: List<ExpenseEntry> = emptyList(),
)

@Serializable
data class ExpenseEntry(
    val id: String,
    val name: String,
    val price: Double,
)

@Serializable
data class ExpenseLocation(
    val name: String,
    val city: String? = null,
    val country: String? = null,
    val street: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
