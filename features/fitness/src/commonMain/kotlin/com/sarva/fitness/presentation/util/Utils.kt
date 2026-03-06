package com.sarva.fitness.presentation.util

import com.sarva.fitness.domain.model.ActivityPeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun formatPeriodLabel(period: ActivityPeriod, anchorDate: LocalDate): String {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    return when (period) {
        ActivityPeriod.DAY -> {
            val dayOfWeek = anchorDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
            val monthName = anchorDate.month.name.lowercase().replaceFirstChar { it.uppercase() }

            if (anchorDate.year == currentYear) {
                "$dayOfWeek, $monthName ${anchorDate.day}"
            } else {
                "$dayOfWeek, $monthName ${anchorDate.day}, ${anchorDate.year}"
            }
        }

        ActivityPeriod.WEEK -> {
            val daysToMonday = anchorDate.dayOfWeek.isoDayNumber - 1
            val monday = anchorDate.minus(daysToMonday, DateTimeUnit.DAY)
            val sunday = monday.plus(6, DateTimeUnit.DAY)

            val startMonth = monday.month.name.lowercase().replaceFirstChar { it.uppercase() }
            if (monday.month == sunday.month) {
                val dateRange = "$startMonth ${monday.day}-${sunday.day}"
                if (monday.year == currentYear) dateRange else "$dateRange, ${monday.year}"
            } else {
                val endMonth = sunday.month.name.lowercase().replaceFirstChar { it.uppercase() }
                if (monday.year == sunday.year) {
                    val range = "$startMonth ${monday.day} - $endMonth ${sunday.day}"
                    if (monday.year == currentYear) range else "$range, ${monday.year}"
                } else {
                    "$startMonth ${monday.day}, ${monday.year} - $endMonth ${sunday.day}, ${sunday.year}"
                }
            }
        }

        ActivityPeriod.MONTH -> {
            val monthName = anchorDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
            "$monthName ${anchorDate.year}"
        }

        ActivityPeriod.YEAR -> {
            anchorDate.year.toString()
        }
    }
}

fun getDaysInMonth(month: Month, year: Int): Int {
    return when (month) {
        Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}

fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

fun getShortDayName(dayOfWeek: DayOfWeek): String {
    return dayOfWeek.name.take(3)
        .lowercase()
        .replaceFirstChar { it.uppercase() }
}