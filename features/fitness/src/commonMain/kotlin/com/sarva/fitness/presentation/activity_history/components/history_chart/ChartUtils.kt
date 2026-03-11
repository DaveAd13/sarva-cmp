package com.sarva.fitness.presentation.activity_history.components.history_chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sarva.features.fitness.generated.resources.Res
import com.sarva.features.fitness.generated.resources.months
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.BarItem
import com.sarva.fitness.domain.model.ChartUiData
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.FitnessRecordType
import com.sarva.fitness.domain.model.FitnessRecords
import com.sarva.fitness.presentation.util.getDaysInMonth
import com.sarva.fitness.presentation.util.getShortDayName
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
fun rememberChartData(
    fitnessActivity: FitnessActivity,
    period: ActivityPeriod,
    anchorDate: LocalDate,
    recordType: FitnessRecordType,
    stepGoal: Int = 0
): ChartUiData {

    val months = stringArrayResource(Res.array.months)

    return remember(fitnessActivity, period, anchorDate, recordType, months) {
        val records = fitnessActivity.records

        fun getValue(record: FitnessRecords): Float = when (recordType) {
            FitnessRecordType.STEPS -> record.steps.toFloat()
            FitnessRecordType.CALORIES -> record.calories.toFloat()
            FitnessRecordType.DISTANCE -> record.distance.toFloat()
        }

        val bars = when (period) {
            ActivityPeriod.DAY -> {
                (0 until 24).map { hour ->
                    val record = records.getOrNull(hour)
                    val value = record?.let { getValue(it) } ?: 0f
                    val label = when {
                        hour == 0 || hour == 23 -> "$hour"
                        hour % 4 == 0 && hour != 24 -> "$hour"
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
        val showGoal = recordType == FitnessRecordType.STEPS && (period == ActivityPeriod.WEEK || period == ActivityPeriod.MONTH)
        val maxY = calculateMaxY(maxVal, stepGoal.toFloat(), showGoal, recordType, period)

        ChartUiData(bars, maxY, showGoal, stepGoal)
    }
}

fun calculateMaxY(
    maxVal: Float,
    goalValue: Float,
    showGoal: Boolean,
    recordType: FitnessRecordType,
    period: ActivityPeriod
): Float {
    val referenceValue = if (showGoal) maxOf(maxVal, goalValue) else maxVal

    val minFloor = when (recordType) {
        FitnessRecordType.STEPS -> when (period) {
            ActivityPeriod.DAY -> 50f
            ActivityPeriod.YEAR -> 100000f
            else -> 2000f
        }
        FitnessRecordType.CALORIES -> when (period) {
            ActivityPeriod.DAY -> 50f
            ActivityPeriod.YEAR -> 30000f
            else -> 500f
        }
        FitnessRecordType.DISTANCE -> when (period) {
            ActivityPeriod.DAY -> 0.5f
            ActivityPeriod.YEAR -> 50f
            else -> 2f
        }
    }

    val rawMax = maxOf(referenceValue, minFloor) * 1.1f
    val magnitude = 10f.pow(floor(log10(rawMax)))

    val step = when {
        rawMax < 10f -> 1f
        rawMax < 50f -> 5f
        rawMax < 200f -> 20f
        rawMax < 1000f -> 100f
        rawMax < 5000f -> 500f
        period == ActivityPeriod.YEAR -> (magnitude / 2f)
        else -> magnitude / 10f
    }

    return ceil(rawMax / step) * step
}