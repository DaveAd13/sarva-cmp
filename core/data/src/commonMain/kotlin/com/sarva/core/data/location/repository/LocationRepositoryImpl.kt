package com.sarva.core.data.location.repository

import com.sarva.common.DispatcherProvider
import com.sarva.core.data.location.remote.LocationApi
import com.sarva.core.data.location.remote.dto.toDomain
import com.sarva.core.domain.model.location.LocationSearchResult
import com.sarva.core.domain.repository.LocationRepository
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val api: LocationApi,
    private val dispatchers: DispatcherProvider
) : LocationRepository {

    override suspend fun searchPlaces(query: String): List<LocationSearchResult> =
        withContext(dispatchers.io) {
            val response = api.searchPlaces(query)
            response.features.map { it.toDomain() }
        }
}