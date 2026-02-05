package com.sarva.core.presentation.formatting

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual fun formatCurrency(amount: Double, currencyCode: String): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        this.currencyCode = currencyCode

        minimumFractionDigits = (if (amount % 1.0 == 0.0) 0 else 2).toULong()
        maximumFractionDigits = 2u
    }

    // Force space between symbol and number
    val format = formatter.positiveFormat
    formatter.positiveFormat = format
        .replace("¤", "¤ ")
        .replace("  ", " ")

    return formatter.stringFromNumber(NSNumber(amount)) ?: ""
}

