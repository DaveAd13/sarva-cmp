package com.sarva.expenses.presentation.expense_add_edit.components

import androidx.compose.foundation.text.input.InputTransformation

val CurrencyInputTransformation  = InputTransformation {
    val newText = asCharSequence().toString()

    if (newText.isEmpty()) return@InputTransformation

    if (!newText.all { it.isDigit() || it == '.' } || newText.count { it == '.' } > 1) {
        revertAllChanges()
        return@InputTransformation
    }

    if (newText == ".") {
        replace(0, length, "0.")
        return@InputTransformation
    }

    if (newText.length > 1 && newText.startsWith("0") && newText[1] != '.') {
        replace(0, 2, newText[1].toString())
        return@InputTransformation
    }

    val dotIndex = newText.indexOf('.')
    if (dotIndex != -1) {
        val decimals = newText.substring(dotIndex + 1)
        if (decimals.length > 2) {
            revertAllChanges()
            return@InputTransformation
        }
    }
}