package com.sarva.core.domain.location.repository

import com.sarva.core.domain.location.model.LocationSearchResult

interface LocationRepository {
    suspend fun searchPlaces(query: String): List<LocationSearchResult>
}