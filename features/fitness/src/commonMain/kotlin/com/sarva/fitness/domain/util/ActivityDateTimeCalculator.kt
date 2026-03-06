package com.sarva.fitness.domain.util

import com.sarva.fitness.domain.model.ActivityPeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

object ActivityDateTimeCalculator {

    fun calculateNewDate(
        period: ActivityPeriod,
        current: LocalDate,
        steps: Int
    ): LocalDate {
        return when (period) {
            ActivityPeriod.DAY -> current.plus(steps, DateTimeUnit.DAY)
            ActivityPeriod.WEEK -> current.plus(steps, DateTimeUnit.WEEK)
            ActivityPeriod.MONTH -> current.plus(steps, DateTimeUnit.MONTH)
            ActivityPeriod.YEAR -> current.plus(steps, DateTimeUnit.YEAR)
        }
    }

    fun canGoForward(period: ActivityPeriod, anchorDate: LocalDate): Boolean {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return when (period) {
            ActivityPeriod.DAY -> anchorDate < today
            ActivityPeriod.WEEK -> {
                val anchorMonday =
                    anchorDate.minus(anchorDate.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
                val todayMonday = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
                anchorMonday < todayMonday
            }

            ActivityPeriod.MONTH -> {
                anchorDate.year < today.year || (anchorDate.year == today.year && anchorDate.month < today.month)
            }

            ActivityPeriod.YEAR -> anchorDate.year < today.year
        }
    }
}