package com.sarva.fitness.domain.model

import com.sarva.core.domain.util.atStartOfDay
import com.sarva.core.domain.util.plusDays
import com.sarva.core.domain.util.plusMonths
import com.sarva.core.domain.util.plusWeeks
import com.sarva.core.domain.util.plusYears
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.isoDayNumber

enum class ActivityPeriod { DAY, WEEK, MONTH, YEAR }

fun ActivityPeriod.calculateRange(anchorDate: LocalDate): Pair<LocalDateTime, LocalDateTime> {
    val startOfDay = anchorDate.atStartOfDay()

    return when (this) {
        ActivityPeriod.DAY -> {
            // Start of today -> Start of tomorrow
            startOfDay to startOfDay.plusDays(1)
        }

        ActivityPeriod.WEEK -> {
            // Find Monday
            val daysToMonday = anchorDate.dayOfWeek.isoDayNumber - 1
            val monday = startOfDay.plusDays(-daysToMonday)
            monday to monday.plusWeeks(1)
        }

        ActivityPeriod.MONTH -> {
            // 1st of Month -> 1st of Next Month
            val firstDay = LocalDate(anchorDate.year, anchorDate.month, 1).atStartOfDay()
            firstDay to firstDay.plusMonths(1)
        }

        ActivityPeriod.YEAR -> {
            // Jan 1 -> Jan 1 Next Year
            val firstDay = LocalDate(anchorDate.year, 1, 1).atStartOfDay()
            firstDay to firstDay.plusYears(1)
        }
    }
}