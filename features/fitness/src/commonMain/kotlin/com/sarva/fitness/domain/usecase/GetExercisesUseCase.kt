package com.sarva.fitness.domain.usecase

import com.sarva.core.domain.util.Result
import com.sarva.fitness.domain.model.FitnessExercise
import com.sarva.fitness.domain.repository.FitnessRepository
import kotlinx.datetime.LocalDateTime
import kotlin.coroutines.cancellation.CancellationException

class GetExercisesUseCase(
    private val repository: FitnessRepository
) {
    suspend operator fun invoke(
        from: LocalDateTime,
        to: LocalDateTime
    ): Result<List<FitnessExercise>> {
        return try {
            val response = repository.getExercises(from, to)
            Result.Success(response)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Failure(e)
        }
    }
}