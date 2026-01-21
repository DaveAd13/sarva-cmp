package com.sarva.fitness.domain.model

import kotlinx.datetime.LocalDate

data class FitnessRecords(
    val date: LocalDate,
    val steps: Long = 0,
    val calories: Double = 0.0,
    val distance: Double = 0.0
)