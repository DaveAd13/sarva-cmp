package com.sarva.fitness.domain.usecase

import com.sarva.core.domain.util.Result
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.calculateRange
import com.sarva.fitness.domain.repository.FitnessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class GetActivityHistoryUseCase(
    private val repository: FitnessRepository
) {
    suspend operator fun invoke(
        period: ActivityPeriod,
        anchorDate: LocalDate
    ): Result<FitnessActivity> = withContext(Dispatchers.IO) {
        try {
            val (from, to) = period.calculateRange(anchorDate)
            val records = repository.getRecordsHistory(period, anchorDate)
            val exercises = repository.getExercises(from, to)

            Result.Success(
                FitnessActivity(records, exercises)
            )

        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(e)
        }
    }
}