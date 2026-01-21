package com.sarva.fitness.presentation.activity_history.components

import com.sarva.core.presentation.util.UiText
import com.sarva.features.fitness.generated.resources.Res
import com.sarva.features.fitness.generated.resources.day
import com.sarva.features.fitness.generated.resources.month
import com.sarva.features.fitness.generated.resources.week
import com.sarva.features.fitness.generated.resources.year
import com.sarva.fitness.domain.model.ActivityPeriod

data class PeriodTabItem(
    val activityPeriod: ActivityPeriod,
    val label: UiText
)

val PERIOD_TABS = listOf(
    PeriodTabItem(
        activityPeriod = ActivityPeriod.DAY,
        label = UiText.StringRes(Res.string.day)
    ),
    PeriodTabItem(
        activityPeriod = ActivityPeriod.WEEK,
        label = UiText.StringRes(Res.string.week)
    ),
    PeriodTabItem(
        activityPeriod = ActivityPeriod.MONTH,
        label = UiText.StringRes(Res.string.month)
    ),
    PeriodTabItem(
        activityPeriod = ActivityPeriod.YEAR,
        label = UiText.StringRes(Res.string.year)
    ),
)
