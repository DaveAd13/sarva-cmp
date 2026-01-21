package com.sarva.fitness.data.repository

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsBike
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Hiking
import androidx.compose.material.icons.rounded.Pool
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.SportsGymnastics
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessExercise
import com.sarva.fitness.domain.model.FitnessRecords
import com.sarva.fitness.domain.repository.FitnessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.number
import java.time.DayOfWeek
import java.time.Duration
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import kotlinx.datetime.LocalDate as KLocalDate
import kotlinx.datetime.LocalDateTime as KLocalDateTime
import java.time.LocalDate as JLocalDate
import java.time.LocalDateTime as JLocalDateTime

class AndroidFitnessRepository(
    private val context: Context
) : FitnessRepository {

    private val healthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }

    override suspend fun hasPermissions(): Boolean {
        val granted = healthConnectClient
            .permissionController
            .getGrantedPermissions()

        return granted.contains(
            HealthPermission.getReadPermission(StepsRecord::class),
        ) && granted.contains(
            HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        ) && granted.contains(
            HealthPermission.getReadPermission(DistanceRecord::class),
        ) && granted.contains(
            HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        )
    }

    override suspend fun getDailyRecords(): FitnessRecords = withContext(Dispatchers.IO) {

        val today = JLocalDate.now(ZoneId.systemDefault())

        val start = today.atStartOfDay()
        val end = JLocalDateTime.now(ZoneId.systemDefault())

        val request = AggregateRequest(
            metrics = setOf(
                StepsRecord.COUNT_TOTAL,
                TotalCaloriesBurnedRecord.ENERGY_TOTAL,
                DistanceRecord.DISTANCE_TOTAL
            ),
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )

        val response = healthConnectClient.aggregate(request)

        FitnessRecords(
            date = KLocalDate(today.year, today.monthValue, today.dayOfMonth),
            steps = response[StepsRecord.COUNT_TOTAL] ?: 0L,
            calories = response[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0,
            distance = response[DistanceRecord.DISTANCE_TOTAL]?.inKilometers ?: 0.0
        )
    }

    override suspend fun getRecordsHistory(
        period: ActivityPeriod,
        anchorDate: KLocalDate
    ): List<FitnessRecords> = withContext(Dispatchers.IO) {
        val zoneId = ZoneId.systemDefault()
        // 1. Get "Now" to filter future data
        val now = ZonedDateTime.now(zoneId)

        // Convert Common LocalDate -> Java LocalDate
        val javaDate = JLocalDate.of(
            anchorDate.year,
            anchorDate.month.number,
            anchorDate.day
        )

        // 1. Calculate the Time Window & Slicer based on Period
        val (startLocal, endLocal, slicer) = when (period) {
            ActivityPeriod.DAY -> {
                val start = javaDate.atStartOfDay()
                Triple(start, start.plusDays(1), Duration.ofHours(1))
            }
            ActivityPeriod.WEEK -> {
                val monday = javaDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val start = monday.atStartOfDay()
                Triple(start, start.plusWeeks(1), Period.ofDays(1))
            }
            ActivityPeriod.MONTH -> {
                val start = javaDate.withDayOfMonth(1).atStartOfDay()
                Triple(start, start.plusMonths(1), Period.ofDays(1))
            }
            ActivityPeriod.YEAR -> {
                val start = javaDate.withDayOfYear(1).atStartOfDay()
                Triple(start, start.plusYears(1), Period.ofMonths(1))
            }
        }

        val metricsToFetch = setOf(
            StepsRecord.COUNT_TOTAL,
            TotalCaloriesBurnedRecord.ENERGY_TOTAL,
            DistanceRecord.DISTANCE_TOTAL
        )

        val responseList = if (period == ActivityPeriod.DAY) {
            val request = AggregateGroupByDurationRequest(
                metrics = metricsToFetch,
                timeRangeFilter = TimeRangeFilter.between(startLocal, endLocal),
                timeRangeSlicer = slicer as Duration
            )
            val res = healthConnectClient.aggregateGroupByDuration(request)

            res.map { bucket ->
                val bucketTime = bucket.startTime.atZone(zoneId)

                // 2. CHECK IF FUTURE: If the bucket starts after "now", force 0 values
                val isFuture = bucketTime.isAfter(now)

                val steps = if (isFuture) 0L else bucket.result[StepsRecord.COUNT_TOTAL] ?: 0L
                val calories = if (isFuture) 0.0 else bucket.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0
                val distance = if (isFuture) 0.0 else bucket.result[DistanceRecord.DISTANCE_TOTAL]?.inKilometers ?: 0.0

                FitnessRecords(
                    date = KLocalDate(
                        bucketTime.year,
                        bucketTime.monthValue,
                        bucketTime.dayOfMonth,
                        // Note: You might want to pass hour here if your KLocalDate supports time,
                        // or use a separate field for Hourly views.
                    ),
                    steps = steps,
                    calories = calories,
                    distance = distance
                )
            }
        } else {
            val request = AggregateGroupByPeriodRequest(
                metrics = metricsToFetch,
                timeRangeFilter = TimeRangeFilter.between(startLocal, endLocal),
                timeRangeSlicer = slicer as Period
            )
            val res = healthConnectClient.aggregateGroupByPeriod(request)

            res.map { bucket ->
                val bucketTime = bucket.startTime.atZone(zoneId)

                // 2. CHECK IF FUTURE
                val isFuture = bucketTime.isAfter(now)

                val steps = if (isFuture) 0L else bucket.result[StepsRecord.COUNT_TOTAL] ?: 0L
                val calories = if (isFuture) 0.0 else bucket.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0
                val distance = if (isFuture) 0.0 else bucket.result[DistanceRecord.DISTANCE_TOTAL]?.inKilometers ?: 0.0

                FitnessRecords(
                    date = KLocalDate(
                        bucketTime.year,
                        bucketTime.monthValue,
                        bucketTime.dayOfMonth
                    ),
                    steps = steps,
                    calories = calories,
                    distance = distance
                )
            }
        }

        responseList.sortedBy { it.date }
    }

    override suspend fun getExercises(
        startDate: KLocalDateTime,
        endDate: KLocalDateTime
    ): List<FitnessExercise> = withContext(Dispatchers.IO) {
        val zoneId = ZoneId.systemDefault()

        val startTime = JLocalDateTime.of(
            startDate.year, startDate.month.number, startDate.day, startDate.hour, startDate.minute
        ).atZone(zoneId).toInstant()

        val endTime = JLocalDateTime.of(
            endDate.year, endDate.month.number, endDate.day, endDate.hour, endDate.minute
        ).atZone(zoneId).toInstant()

        val request = ReadRecordsRequest(
            recordType = ExerciseSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        )

        val response = healthConnectClient.readRecords(request)

        response.records.map { record ->
            val duration = (record.endTime.toEpochMilli() - record.startTime.toEpochMilli()) / 1000
            val recordStart = record.startTime.atZone(zoneId).toLocalDateTime()
            val recordEnd = record.endTime.atZone(zoneId).toLocalDateTime()

            val (type, icon) = formatExerciseType(record.exerciseType)

            FitnessExercise(
                id = record.metadata.id,
                type = type,
                icon = icon,
                startTime = KLocalDateTime(
                    recordStart.year, recordStart.monthValue, recordStart.dayOfMonth,
                    recordStart.hour, recordStart.minute
                ),
                endTime = KLocalDateTime(
                    recordEnd.year, recordEnd.monthValue, recordEnd.dayOfMonth,
                    recordEnd.hour, recordEnd.minute
                ),
                durationSeconds = duration.toInt(),
            )
        }.sortedByDescending { it.startTime }
    }

    private fun formatExerciseType(type: Int): Pair<String, ImageVector> {
        return when (type) {
            ExerciseSessionRecord.EXERCISE_TYPE_WALKING -> ("Walking" to Icons.AutoMirrored.Rounded.DirectionsWalk)
            ExerciseSessionRecord.EXERCISE_TYPE_RUNNING -> ("Running" to Icons.AutoMirrored.Rounded.DirectionsRun)
            ExerciseSessionRecord.EXERCISE_TYPE_BIKING -> ("Cycling" to Icons.AutoMirrored.Rounded.DirectionsBike)
            ExerciseSessionRecord.EXERCISE_TYPE_YOGA -> ("Yoga" to Icons.Rounded.SelfImprovement)
            ExerciseSessionRecord.EXERCISE_TYPE_GYMNASTICS -> ("Gymnastics" to Icons.Rounded.SportsGymnastics)
            ExerciseSessionRecord.EXERCISE_TYPE_HIKING -> ("Hiking" to Icons.Rounded.Hiking)
            ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_POOL -> ("Swimming" to Icons.Rounded.Pool)
            else -> ("Workout" to Icons.Rounded.FitnessCenter)
        }
    }
}