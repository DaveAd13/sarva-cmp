package com.sarva.fitness.domain.usecase

import com.sarva.fitness.domain.model.FitnessRecords
import com.sarva.fitness.domain.repository.FitnessRepository
import com.sarva.core.domain.util.Result

class GetDailyRecordsUseCase(
    private val repository: FitnessRepository
) {
    suspend operator fun invoke(): Result<FitnessRecords> {
        return try {
            val response = repository.getDailyRecords()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}