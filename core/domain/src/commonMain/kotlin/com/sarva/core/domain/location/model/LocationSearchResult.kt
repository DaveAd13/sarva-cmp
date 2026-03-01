package com.sarva.core.domain.location.model

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