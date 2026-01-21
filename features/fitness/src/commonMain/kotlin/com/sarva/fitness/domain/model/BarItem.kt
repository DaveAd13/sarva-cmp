package com.sarva.fitness.domain.model

import kotlinx.datetime.LocalDate

data class BarItem(
    val value: Float,
    val label: String,
    val fullDate: LocalDate?
)
