package com.sarva.core.data.currencies.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CurrencyDto(
    val symbol: String,
    val name: String,
    @SerialName("symbol_native")
    val symbolNative: String,
    val code: String,
    @SerialName("country_code")
    val countryCode: String
)