package com.sarva.fitness.domain.usecase

import com.sarva.core.domain.util.Result
import com.sarva.fitness.domain.model.FitnessRecords
import com.sarva.fitness.domain.repository.FitnessRepository
import kotlin.coroutines.cancellation.CancellationException

class GetDailyRecordsUseCase(
    private val repository: FitnessRepository
) {
    suspend operator fun invoke(): Result<FitnessRecords> {
        return try {
            val response = repository.getDailyRecords()
            Result.Success(response)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Failure(e)
        }
    }
}