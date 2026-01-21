package com.sarva.core.domain.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalDate.atStartOfDay(): LocalDateTime {
    return LocalDateTime(this, LocalTime(0, 0))
}

fun LocalDateTime.plusDays(days: Int): LocalDateTime {
    val period = DatePeriod(days = days)
    return this.toInstant(TimeZone.currentSystemDefault())
        .plus(period, TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.plusWeeks(weeks: Int): LocalDateTime {
    return this.plusDays(weeks * 7)
}

fun LocalDateTime.plusMonths(months: Int): LocalDateTime {
    val period = DatePeriod(months = months)
    return this.toInstant(TimeZone.currentSystemDefault())
        .plus(period, TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.plusYears(years: Int): LocalDateTime {
    val period = DatePeriod(years = years)
    return this.toInstant(TimeZone.currentSystemDefault())
        .plus(period, TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault())
}