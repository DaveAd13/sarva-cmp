package com.sarva.app.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sarva.app.features.calendar.presentation.CalendarRoot
import com.sarva.app.features.home.presentation.HomeNavigationAction
import com.sarva.app.features.notes.presentation.note_add_edit.NoteAddEditRoot
import com.sarva.app.features.notes.presentation.note_details.NoteDetailsRoot
import com.sarva.app.features.notes.presentation.note_list.NoteListRoot
import com.sarva.app.features.places.presentation.PlacesRoot
import com.sarva.app.features.profile.presentation.ProfileRoot
import com.sarva.app.features.tasks.presentation.task_list.TaskListRoot
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.rememberResultStore
import com.sarva.expenses.presentation.expense_add_edit.ExpenseAddEditRoot
import com.sarva.expenses.presentation.expense_details.ExpenseDetailsRoot
import com.sarva.expenses.presentation.expense_list.ExpenseListRoot
import com.sarva.fitness.presentation.activity_history.ActivityHistoryRoot
import com.sarva.fitness.presentation.daily_activity.DailyActivityRoot

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {

    val navigationState = rememberNavigationState(
        startRoute = Route.Main,
        topLevelRoutes = setOf(Route.Main),
    )

    val navigator = remember { Navigator(navigationState) }
    val resultStore = rememberResultStore()

    CompositionLocalProvider(
        LocalBackHandler provides navigator::goBack
    ) {
        NavDisplay(
            modifier = modifier,
            onBack = navigator::goBack,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            popTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Main> {
                        MainRoot(
                            onNavigate = { action ->
                                when (action) {
                                    HomeNavigationAction.OpenNotes -> navigator.navigate(Route.NoteList)
                                    HomeNavigationAction.OpenFitness -> navigator.navigate(Route.FitnessDailyActivity)
                                    HomeNavigationAction.OpenTasks -> navigator.navigate(Route.TaskList)
                                    HomeNavigationAction.OpenExpenses -> navigator.navigate(Route.ExpenseList)
                                    HomeNavigationAction.OpenCalendar -> navigator.navigate(Route.Calendar)
                                    HomeNavigationAction.OpenPlaces -> navigator.navigate(Route.Places)
                                }
                            }
                        )
                    }
                    entry<Route.NoteList> {
                        NoteListRoot(
                            onNoteClick = { noteId ->
                                navigator.navigate(Route.NoteDetails(noteId = noteId))
                            },
                            onAddNoteClick = {
                                navigator.navigate(Route.NoteAddEdit(noteId = null))
                            },
                        )
                    }
                    entry<Route.NoteDetails> {
                        NoteDetailsRoot(
                            noteId = it.noteId,
                            onEditClick = {
                                navigator.navigate(Route.NoteAddEdit(noteId = it.noteId))
                            },
                        )
                    }

                    entry<Route.NoteAddEdit> {
                        NoteAddEditRoot(
                            noteId = it.noteId,
                            onSaveFinished = {
                                navigator.goBack()
                            },
                        )
                    }

                    entry<Route.ExpenseList> {
                        ExpenseListRoot(
                            resultStore = resultStore,
                            onExpenseClick = { expenseId ->
                                navigator.navigate(Route.ExpenseDetails(expenseId = expenseId))
                            },
                            onAddExpenseClick = {
                                navigator.navigate(Route.ExpenseAddEdit(expenseId = null))
                            },
                        )
                    }

                    entry<Route.ExpenseDetails> { key ->
                        ExpenseDetailsRoot(
                            expenseId = key.expenseId,
                            resultStore = resultStore
                        )
                    }

                    entry<Route.ExpenseAddEdit> { key ->
                        ExpenseAddEditRoot(
                            expenseId = key.expenseId,
                            resultStore = resultStore,
                        )
                    }

                    entry<Route.TaskList> {
                        TaskListRoot()
                    }

                    entry<Route.FitnessDailyActivity> {
                        DailyActivityRoot(
                            onOpenFitnessDetails = { metricType ->
                                navigator.navigate(Route.FitnessActivityHistory(metricType))
                            },
                        )
                    }

                    entry<Route.FitnessActivityHistory> { key ->
                        ActivityHistoryRoot(
                            recordType = key.fitnessRecordType,
                        )
                    }

                    entry<Route.Places> {
                        PlacesRoot()
                    }

                    entry<Route.Calendar> {
                        CalendarRoot()
                    }

                    entry<Route.More> {
                        ProfileRoot()
                    }
                }

            )
        )
    }
}