package com.sarva.core.data.expenses.mapper

import com.sarva.core.data.expenses.local.entity.ExpenseEntity
import com.sarva.core.domain.expenses.model.Expense


fun ExpenseEntity.toDomain() = Expense(
    id = id,
    title = title,
    category = category,
    amount = amount,
    currency = currency,
    dateTime = dateTime,
    location = location,
    entries = entries,
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    title = title,
    category = category,
    amount = amount,
    currency = currency,
    dateTime = dateTime,
    location = location,
    entries = entries,
)