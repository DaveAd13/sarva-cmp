package com.sarva.fitness.presentation.activity_history.components.history_chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sarva.features.fitness.generated.resources.Res
import com.sarva.features.fitness.generated.resources.months
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.BarItem
import com.sarva.fitness.domain.model.ChartTransition
import com.sarva.fitness.domain.model.ChartUiData
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.FitnessRecordType
import com.sarva.fitness.domain.model.FitnessRecords
import com.sarva.fitness.presentation.activity_history.ActivityHistoryState
import com.sarva.fitness.presentation.util.getDaysInMonth
import com.sarva.fitness.presentation.util.getShortDayName
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.stringArrayResource
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@Composable
fun rememberChartDataLatched(
    state: ActivityHistoryState
): ChartUiData {
    // 1. Calculate the potential new data
    val newData = rememberChartData(
        fitnessActivity = state.fitnessActivity,
        period = state.period,
        anchorDate = state.anchorDate,
        recordType = state.recordType,
        transition = state.transition

    )

    // 2. Create a holder for the last *valid* data
    // We initialize it with empty, but once it has data, we hold it.
    var latchedData by remember { mutableStateOf(newData) }

    // 3. Only update the latch if the new data is NOT empty and NOT loading
    // This prevents the "empty flash" when switching periods
    if (!state.isLoading && newData.bars.isNotEmpty()) {
        latchedData = newData
    }

    // 4. Always return the latched data (which is the old valid data during loading)
    return latchedData
}

@Composable
fun rememberChartData(
    fitnessActivity: FitnessActivity,
    period: ActivityPeriod,
    anchorDate: LocalDate,
    recordType: FitnessRecordType,
    transition: ChartTransition
): ChartUiData {

    val months = stringArrayResource(Res.array.months)

    return remember(fitnessActivity, period, anchorDate, recordType, months) {
        val records = fitnessActivity.records

        val isDataMismatched = when (period) {
            ActivityPeriod.DAY -> records.isNotEmpty() && records.size < 24
            ActivityPeriod.WEEK -> records.isNotEmpty() && records.size != 7
            ActivityPeriod.MONTH -> records.isNotEmpty() && (records.size !in 28..31)
            ActivityPeriod.YEAR -> records.isNotEmpty() && records.size != 12
        }

        if (isDataMismatched) {
            return@remember ChartUiData(persistentListOf(), 1f, transition)
        }

        fun getValue(record: FitnessRecords): Float = when (recordType) {
            FitnessRecordType.STEPS -> record.steps.toFloat()
            FitnessRecordType.CALORIES -> record.calories.toFloat()
            FitnessRecordType.DISTANCE -> record.distance.toFloat()
        }

        val bars = when (period) {
            ActivityPeriod.DAY -> {
                (0..24).map { hour ->
                    val record = records.getOrNull(hour)
                    val value = record?.let { getValue(it) } ?: 0f
                    val label = when {
                        hour % 4 == 0 -> "$hour"
                        hour % 2 == 0 -> "•"
                        else -> ""
                    }
                    BarItem(value, label, anchorDate)
                }
            }

            ActivityPeriod.WEEK -> {
                val startOfWeek = anchorDate.minus(anchorDate.dayOfWeek.ordinal, DateTimeUnit.DAY)

                (0..6).map { offset ->
                    val targetDate = startOfWeek.plus(offset, DateTimeUnit.DAY)
                    val record = records.find { it.date == targetDate }

                    BarItem(
                        value = record?.let { getValue(it) } ?: 0f,
                        label = getShortDayName(targetDate.dayOfWeek),
                        fullDate = targetDate
                    )
                }
            }

            ActivityPeriod.MONTH -> {
                val daysInMonth = getDaysInMonth(anchorDate.month, anchorDate.year)

                (1..daysInMonth).map { day ->
                    val targetDate = LocalDate(anchorDate.year, anchorDate.month, day)
                    val record = records.find { it.date == targetDate }

                    val label = when {
                        day == 1 -> "1"
                        day % 5 == 0 && day < daysInMonth - 1 -> day.toString()
                        day == daysInMonth -> day.toString()
                        else -> ""
                    }

                    BarItem(
                        value = record?.let { getValue(it) } ?: 0f,
                        label = label,
                        fullDate = targetDate
                    )
                }
            }

            ActivityPeriod.YEAR -> {
                val yearRecords = records.filter { it.date.year == anchorDate.year }

                (1..12).map { monthNum ->
                    val monthTotal = yearRecords
                        .filter { it.date.month.number == monthNum }
                        .sumOf { getValue(it).toDouble() }
                        .toFloat()

                    BarItem(
                        value = monthTotal,
                        label = months[monthNum - 1],
                        fullDate = LocalDate(anchorDate.year, monthNum, 1)
                    )
                }
            }
        }

        val maxVal = bars.maxOfOrNull { it.value } ?: 0f
        val maxY = calculateMaxY(maxVal * 1.2f)

        ChartUiData(bars, maxY, transition)
    }
}

fun calculateMaxY(maxVal: Float): Float {
    if (maxVal <= 0f) return 10f
    if (maxVal < 10f) return maxVal

    val magnitude = 10f.pow(floor(log10(maxVal)))

    val step = when {
        maxVal < 100f -> 10f
        maxVal < 1000f -> 100f
        else -> magnitude / 10f
    }

    return ceil(maxVal / step) * step
}