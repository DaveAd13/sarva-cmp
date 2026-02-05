package com.sarva.core.presentation.formatting

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

actual fun formatCurrency(amount: Double, currencyCode: String): String {
    val locale = Locale.getDefault()
    val format = NumberFormat.getCurrencyInstance(locale)
    format.currency = Currency.getInstance(currencyCode)

    if (format is java.text.DecimalFormat) {
        val pattern = format.toPattern()
        // The '¤' is the currency symbol.
        // We replace it with '¤ ' or ' ¤' depending on where it is.
        val spacedPattern = pattern
            .replace("¤#", "¤ #")   // Space for prefix (English style)
            .replace("#¤", "# ¤")   // Space for suffix (Armenian style)
            .replace("¤n", "¤ n")

        format.applyPattern(spacedPattern)
    }

    format.minimumFractionDigits = if (amount % 1.0 == 0.0) 0 else 2
    return format.format(amount)
}