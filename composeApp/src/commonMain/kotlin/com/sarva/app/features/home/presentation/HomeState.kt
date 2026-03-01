package com.sarva.app.features.home.presentation

import androidx.compose.runtime.Stable
import com.sarva.app.features.calendar.domain.model.CalendarEvent
import com.sarva.core.domain.settings.model.WidgetLayout
import com.sarva.app.features.home.domain.model.SpentInfo
import com.sarva.app.features.notes.domain.model.Note
import com.sarva.app.features.tasks.domain.model.Task
import com.sarva.fitness.presentation.daily_activity.DailyActivityViewModel.Companion.STEP_GOAL

@Stable
data class HomeState(
    val isLoading: Boolean = false,
    val widgetLayout: WidgetLayout = WidgetLayout.TILED,
    val steps: Int = 0,
    val calories: String = "0",
    val distance: String = "0",
    val stepsGoal: Int = STEP_GOAL,
    val hasHealthPermission: Boolean = false,
    val tasks: List<Task> = listOf(
        Task("1", true, "Walk Bruno"),
        Task("2", false, "Buy groceries"),
        Task("3", false, "Call dentist"),
    ),
    val event: CalendarEvent? = CalendarEvent(
        title = "Birthday party!",
        date = 123123L
    ),
    val spentInfo: SpentInfo = SpentInfo(
        totalSpent = 458.0,
        currency = "$",
        recentSpendingTrend = listOf(0.3f, 0.6f, 0.2f, 0.8f, 0.5f)
    ),
    val notesCount: Int = 0,
    val recentNote: Note? = Note(
        "Meeting notes",
        "1.Do something\n2.Do something else\n3.Do something more\n4.Do nothing",
        123123L
    )
)