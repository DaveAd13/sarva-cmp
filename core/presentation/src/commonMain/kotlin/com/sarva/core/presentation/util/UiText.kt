package com.sarva.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

sealed class UiText {
    data class DynamicString(val value: String) : UiText()

    data class StringRes(
        val res: StringResource,
        val args: List<Any> = emptyList()
    ) : UiText()

    data class PluralRes(
        val res: PluralStringResource,
        val quantity: Int,
        val args: List<Any> = emptyList()
    ) : UiText()

    @Composable
    fun asStringC(): String {
        return when (this) {
            is DynamicString -> value
            is StringRes -> {
                if (args.isEmpty()) stringResource(res)
                else stringResource(res, *args.toTypedArray())
            }
            is PluralRes -> {
                pluralStringResource(res, quantity, *args.toTypedArray())
            }
        }
    }

    suspend fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringRes -> {
                if (args.isEmpty()) getString(res)
                else getString(res, *args.toTypedArray())
            }
            is PluralRes -> {
                getPluralString(res, quantity, *args.toTypedArray())
            }
        }
    }
}