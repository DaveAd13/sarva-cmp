package com.sarva.core.presentation.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun LocalDate.formatToDisplay(): String {
    val day = this.day
    val month = this.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    val dayOfWeek = this.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }

    return "$dayOfWeek, $month $day"
}

fun LocalDateTime.formatToDisplay(): String {
    val day = this.day
    val month = this.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    val dayOfWeek = this.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }

    return "$dayOfWeek, $month $day"
}

@OptIn(ExperimentalTime::class)
fun getFormattedToday(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val day = today.day.toString()
    val month = today.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    val datOfWeek = today.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }

    return "$datOfWeek, $day $month"
}

@OptIn(ExperimentalTime::class)
fun Long.formatToShortDate(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.todayIn(timeZone)

    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(timeZone)

    if (localDateTime.date == today) {
        return "Today"
    }

    val day = localDateTime.day.toString()
    val month = localDateTime.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    val dayOfWeek = localDateTime.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }

    return "$dayOfWeek, $day $month"
}

@OptIn(ExperimentalTime::class)
fun Long.formatToTime(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')

    return "$hour:$minute"
}
