package com.sarva.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ExpenseCategory() {
    FOOD,
    SHOPPING,
    TRANSPORT,
    GROCERIES,
    BILLS,
    SUBSCRIPTIONS,
    HEALTH,
    ENTERTAINMENT,
    TRAVEL,
    SPORTS,
    EDUCATION,
    OTHER,
}