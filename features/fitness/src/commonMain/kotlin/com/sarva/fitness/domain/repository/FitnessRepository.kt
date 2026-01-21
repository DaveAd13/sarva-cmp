package com.sarva.fitness.domain.repository

import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessExercise
import com.sarva.fitness.domain.model.FitnessRecords
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface FitnessRepository {

    suspend fun hasPermissions(): Boolean
    suspend fun getDailyRecords(): FitnessRecords

    suspend fun getRecordsHistory(
        period: ActivityPeriod,
        anchorDate: LocalDate
    ): List<FitnessRecords>

    suspend fun getExercises(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<FitnessExercise>
}