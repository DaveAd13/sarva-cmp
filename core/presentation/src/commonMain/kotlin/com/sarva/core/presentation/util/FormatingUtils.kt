package com.sarva.core.presentation.util

import kotlin.math.pow
import kotlin.math.roundToInt

fun formatNumber(
    number: Number,
    maxDecimals: Int = 2,
    useCommas: Boolean = true,
    hideZeroDecimals: Boolean = true // New parameter
): String {
    val doubleValue = number.toDouble()

    // Handle Rounding
    val factor = 10.0.pow(maxDecimals)
    val rounded = (doubleValue * factor).roundToInt() / factor

    // Split into integer and decimal parts
    val parts = rounded.toString().split(".")
    var integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1].removeSuffix("0") else ""

    // Apply commas to the integer part if requested
    if (useCommas) {
        integerPart = integerPart.reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()
    }

    // Determine if we should show decimals
    return if (decimalPart.isEmpty() || (hideZeroDecimals && decimalPart.all { it == '0' })) {
        integerPart
    } else {
        // Ensure we don't exceed the maxDecimals requested
        val finalDecimal = if (decimalPart.length > maxDecimals) {
            decimalPart.take(maxDecimals)
        } else {
            decimalPart
        }
        "$integerPart.$finalDecimal"
    }
}
