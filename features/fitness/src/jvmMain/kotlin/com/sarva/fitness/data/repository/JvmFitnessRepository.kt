package com.sarva.fitness.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessExercise
import com.sarva.fitness.domain.model.FitnessRecords
import com.sarva.fitness.domain.repository.FitnessRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class JvmFitnessRepository : FitnessRepository {
    override suspend fun hasPermissions(): Boolean {
        return true
    }

    override suspend fun getDailyRecords(): FitnessRecords {
        return FitnessRecords(
            date = LocalDate(2024, 7, 12),
            steps = 5700,
            calories = 134.0,
            distance = 5468.0
        )
    }

    override suspend fun getRecordsHistory(
        period: ActivityPeriod,
        anchorDate: LocalDate
    ): List<FitnessRecords> {
        return listOf(
            FitnessRecords(
                date = LocalDate(2024, 7, 12),
                steps = 5700,
                calories = 134.0,
                distance = 5468.0
            )
        )
    }

    override suspend fun getExercises(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<FitnessExercise> {
        return listOf(
            FitnessExercise(
                id = "1",
                type = "Walking",
                icon = Icons.AutoMirrored.Rounded.DirectionsWalk,
                startTime = LocalDateTime.Companion.parse("2023-01-02T23:40"),
                endTime = LocalDateTime.Companion.parse("2023-01-02T23:40"),
                durationSeconds = 1576
            )
        )
    }
}