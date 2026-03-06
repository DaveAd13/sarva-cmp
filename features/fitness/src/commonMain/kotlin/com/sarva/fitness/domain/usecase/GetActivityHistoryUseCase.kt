package com.sarva.fitness.domain.usecase

import com.sarva.core.domain.util.Result
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.calculateRange
import com.sarva.fitness.domain.repository.FitnessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException

class GetActivityHistoryUseCase(
    private val repository: FitnessRepository
) {
    suspend operator fun invoke(
        period: ActivityPeriod,
        anchorDate: LocalDate
    ): Result<FitnessActivity> = withContext(Dispatchers.IO) {
        try {
            val (from, to) = period.calculateRange(anchorDate)
            val records = async { repository.getRecordsHistory(period, anchorDate) }
            val exercises = async { repository.getExercises(from, to) }

            Result.Success(
                FitnessActivity(records.await(), exercises.await())
            )

        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            Result.Failure(e)
        }
    }
}