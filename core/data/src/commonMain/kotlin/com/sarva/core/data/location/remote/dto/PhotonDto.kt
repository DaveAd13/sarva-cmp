package com.sarva.core.data.location.remote.dto

import com.sarva.core.domain.location.model.LocationSearchResult
import kotlinx.serialization.Serializable

@Serializable
data class PhotonResponse(val features: List<PhotonFeature>)

@Serializable
data class PhotonFeature(val properties: PhotonProps, val geometry: PhotonGeom)

@Serializable
data class PhotonProps(
    val name: String,
    val city: String? = null,
    val country: String? = null,
    val street: String? = null
)

@Serializable
data class PhotonGeom(val coordinates: List<Double>)

fun PhotonFeature.toDomain() = LocationSearchResult(
    name = properties.name,
    city = properties.city,
    country = properties.country,
    street = properties.street,
    latitude = geometry.coordinates[1],
    longitude = geometry.coordinates[0]
)