package com.sarva.core.data.mappers

import com.sarva.core.data.database.expenses.ExpenseEntity
import com.sarva.core.domain.model.Expense


fun ExpenseEntity.toDomain() = Expense(
    id = id,
    title = title,
    category = category,
    amount = amount,
    currency = currency,
    dateTime = dateTime,
    location = location,
    entries = breakdown,
    participants = participants
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    title = title,
    category = category,
    amount = amount,
    currency = currency,
    dateTime = dateTime,
    location = location,
    breakdown = entries,
    participants = participants
)