package com.sarva.fitness.presentation.activity_history

import androidx.compose.runtime.Immutable
import com.sarva.core.presentation.util.UiText
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.ChartTransition
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.FitnessRecordType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Immutable
@OptIn(ExperimentalTime::class)
data class ActivityHistoryState(
    val isLoading: Boolean = false,
    val fitnessActivity: FitnessActivity = FitnessActivity(persistentListOf(), persistentListOf()),
    val period: ActivityPeriod = ActivityPeriod.DAY,
    val anchorDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val recordType: FitnessRecordType = FitnessRecordType.STEPS,
    val periodDateLabel: String = "",
    val periodOverall: String = "0",
    val periodOverallLabel: UiText = UiText.DynamicString(""),
    val canGoForward: Boolean = false,
    val transition: ChartTransition = ChartTransition.DEFAULT
)