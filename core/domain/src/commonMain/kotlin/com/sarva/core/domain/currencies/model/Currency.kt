package com.sarva.core.domain.currencies.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    val symbolNative: String,
    val countryCode: String
)