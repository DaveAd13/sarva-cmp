package com.sarva.core.presentation.formatting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.sarva.core.presentation.generated.resources.Res
import com.sarva.core.presentation.generated.resources.hour_symbol
import com.sarva.core.presentation.generated.resources.minute_symbol
import com.sarva.core.presentation.generated.resources.second_symbol
import org.jetbrains.compose.resources.stringResource

object DurationFormatter {
    fun format(secondsTotal: Int, symbols: DurationSymbols): String {
        val hours = secondsTotal / 3600
        val minutes = (secondsTotal % 3600) / 60
        val seconds = secondsTotal % 60

//        val h = hours.toString().padStart(2, '0')
        val m = minutes.toString().padStart(2, '0')
        val s = seconds.toString().padStart(2, '0')

        return if (hours > 0) {
            "$hours${symbols.hour} $m${symbols.minute} $s${symbols.second}"
        } else {
            "$m${symbols.minute} $s${symbols.second}"
        }
    }
}

@Immutable
data class DurationSymbols(
    val hour: String,
    val minute: String,
    val second: String
)

@Composable
fun rememberDurationSymbols() = DurationSymbols(
    hour = stringResource(Res.string.hour_symbol),
    minute = stringResource(Res.string.minute_symbol),
    second = stringResource(Res.string.second_symbol)
)