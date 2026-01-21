package com.sarva.core.presentation.util

import androidx.compose.runtime.Composable
import com.sarva.core.presentation.generated.resources.Res
import com.sarva.core.presentation.generated.resources.hour_symbol
import com.sarva.core.presentation.generated.resources.minute_symbol
import com.sarva.core.presentation.generated.resources.second_symbol

import org.jetbrains.compose.resources.stringResource

@Composable
fun Int.toFormattedDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    val h = hours.toString().padStart(2, '0')
    val m = minutes.toString().padStart(2, '0')
    val s = seconds.toString().padStart(2, '0')

    val hSymbol = stringResource(Res.string.hour_symbol)
    val mSymbol = stringResource(Res.string.minute_symbol)
    val sSymbol = stringResource(Res.string.second_symbol)

    return if (hours > 0) {
        "$h$hSymbol $m$mSymbol $s$sSymbol"
    } else {
        "$m$mSymbol $s$sSymbol"
    }
}