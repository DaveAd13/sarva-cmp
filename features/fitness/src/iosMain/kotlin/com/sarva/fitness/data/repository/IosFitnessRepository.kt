package com.sarva.fitness.data.repository

import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessExercise
import com.sarva.fitness.domain.model.FitnessRecords
import com.sarva.fitness.domain.repository.FitnessRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.HealthKit.HKHealthStore
import platform.HealthKit.HKObjectType
import platform.HealthKit.HKQuantityType
import platform.HealthKit.HKQuantityTypeIdentifier
import platform.HealthKit.HKQuantityTypeIdentifierActiveEnergyBurned
import platform.HealthKit.HKQuantityTypeIdentifierDistanceWalkingRunning
import platform.HealthKit.HKQuantityTypeIdentifierStepCount
import platform.HealthKit.HKQuery
import platform.HealthKit.HKStatisticsOptionCumulativeSum
import platform.HealthKit.HKStatisticsQuery
import platform.HealthKit.HKUnit
import platform.HealthKit.countUnit
import platform.HealthKit.kilocalorieUnit
import platform.HealthKit.meterUnit
import platform.HealthKit.predicateForSamplesWithStartDate
import kotlin.coroutines.resume

class IosFitnessRepository : FitnessRepository {

    private val healthStore = HKHealthStore()

    override suspend fun hasPermissions(): Boolean {
        // iOS protects user privacy by NOT revealing if "Read" access is granted.
        // We can only check if Health Data is available on the device generally.
        return HKHealthStore.Companion.isHealthDataAvailable()
    }

    override suspend fun getDailyRecords(): FitnessRecords {
        TODO("Not yet implemented")
    }

    override suspend fun getRecordsHistory(
        period: ActivityPeriod,
        anchorDate: LocalDate
    ): List<FitnessRecords> {
        TODO("Not yet implemented")
    }

    override suspend fun getExercises(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<FitnessExercise> {
        TODO("Not yet implemented")
    }

    // Helper to actually ask for permission (You should call this from your UI/ViewModel)
    fun requestPermissions(completion: (Boolean, Error?) -> Unit) {
        val allTypes = setOfNotNull(
            HKObjectType.Companion.quantityTypeForIdentifier(HKQuantityTypeIdentifierStepCount),
            HKObjectType.Companion.quantityTypeForIdentifier(
                HKQuantityTypeIdentifierActiveEnergyBurned
            ),
            HKObjectType.Companion.quantityTypeForIdentifier(
                HKQuantityTypeIdentifierDistanceWalkingRunning
            )
        )

        healthStore.requestAuthorizationToShareTypes(
            typesToShare = null, // We are only reading
            readTypes = allTypes as Set<HKObjectType>
        ) { success, error ->
            completion(success, error as? Error)
        }
    }

    suspend fun getStepsToday(): Result<Long> {
        return runQuery(
            typeIdentifier = HKQuantityTypeIdentifierStepCount,
            unit = HKUnit.Companion.countUnit()
        ).map { it.toLong() }
    }

    suspend fun getCaloriesToday(): Result<Double> {
        return runQuery(
            typeIdentifier = HKQuantityTypeIdentifierActiveEnergyBurned,
            unit = HKUnit.Companion.kilocalorieUnit()
        )
    }

    suspend fun getDistanceToday(): Result<Double> {
        return runQuery(
            typeIdentifier = HKQuantityTypeIdentifierDistanceWalkingRunning,
            unit = HKUnit.Companion.meterUnit()
        ).map { it / 1000.0 } // Convert meters to km if you prefer km, otherwise remove division
    }

    /**
     * A generic helper function to run a Cumulative Sum Query for Today.
     * This saves us from writing the same boilerplate 3 times.
     */
    private suspend fun runQuery(
        typeIdentifier: HKQuantityTypeIdentifier,
        unit: HKUnit
    ): Result<Double> = suspendCancellableCoroutine { continuation ->

        val type = HKQuantityType.Companion.quantityTypeForIdentifier(typeIdentifier)
            ?: run {
                continuation.resume(Result.failure(Exception("Unknown HealthKit Type")))
                return@suspendCancellableCoroutine
            }

        // 1. Define "Today" (Start of current day)
        val now = NSDate()
        val calendar = NSCalendar.Companion.currentCalendar
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = now
        )
        val startDate = calendar.dateFromComponents(components)

        // 2. Create Predicate
        val predicate = HKQuery.Companion.predicateForSamplesWithStartDate(
            startDate = startDate,
            endDate = now,
            options = 0u
        )

        // 3. Create Query
        val query = HKStatisticsQuery(
            quantityType = type,
            quantitySamplePredicate = predicate,
            options = HKStatisticsOptionCumulativeSum
        ) { _, result, error ->
            if (error != null) {
                continuation.resume(Result.failure(Exception(error.localizedDescription)))
            } else {
                val sum = result?.sumQuantity()?.doubleValueForUnit(unit) ?: 0.0
                continuation.resume(Result.success(sum))
            }
        }

        // 4. Execute
        healthStore.executeQuery(query)
    }
}