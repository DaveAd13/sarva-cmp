package com.sarva.core.domain.repository

import com.sarva.core.domain.model.location.LocationSearchResult

interface LocationRepository {
    suspend fun searchPlaces(query: String): List<LocationSearchResult>
}