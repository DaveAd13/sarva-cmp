package com.sarva.core.data.location.remote

import com.sarva.common.getCurrentLocale
import com.sarva.core.data.location.remote.dto.PhotonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class LocationApi(
    private val httpClient: HttpClient
) {
    suspend fun searchPlaces(query: String, limit: Int = 10): PhotonResponse {
        return httpClient.get("https://photon.komoot.io/api/") {
            parameter("q", query)
            parameter("limit", limit)
            parameter("lang", getCurrentLocale())
        }.body()
    }
}