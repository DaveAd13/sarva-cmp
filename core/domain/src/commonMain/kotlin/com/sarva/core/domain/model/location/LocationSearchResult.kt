package com.sarva.core.domain.model.location

import androidx.compose.runtime.Immutable

@Immutable
data class LocationSearchResult(
    val name: String,
    val city: String? = null,
    val country: String? = null,
    val street: String? = null,
    val latitude: Double,
    val longitude: Double
)