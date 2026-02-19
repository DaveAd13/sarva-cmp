package com.sarva.core.data.currencies.mapper

import com.sarva.core.data.currencies.dto.CurrencyDto
import com.sarva.core.domain.model.currency.Currency

internal fun CurrencyDto.toDomain(): Currency {
    return Currency(
        code = this.code,
        name = this.name,
        symbol = this.symbol,
        symbolNative = this.symbolNative,
        countryCode = this.countryCode
    )
}