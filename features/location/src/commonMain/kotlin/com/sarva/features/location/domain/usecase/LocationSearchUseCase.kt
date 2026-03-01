package com.sarva.features.location.domain.usecase

import com.sarva.core.domain.location.model.LocationSearchResult
import com.sarva.core.domain.location.repository.LocationRepository
import com.sarva.core.domain.util.Result
import kotlin.coroutines.cancellation.CancellationException

class LocationSearchUseCase(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(query: String): Result<List<LocationSearchResult>> {
        return try {
            val result = repository.searchPlaces(query)
            Result.Success(result)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Failure(e)
        }
    }
}