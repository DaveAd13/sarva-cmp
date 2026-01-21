package com.sarva.app.navigation

import androidx.navigation3.runtime.NavKey
import com.sarva.fitness.domain.model.FitnessRecordType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object Main : Route

    @Serializable
    data object Home : Route

    //Notes
    @Serializable
    data object NoteList : Route

    @Serializable
    data class NoteDetails(val noteId: String) : Route

    @Serializable
    data class NoteAddEdit(val noteId: String? = null) : Route

    //Expenses
    @Serializable
    data object ExpenseList : Route

    @Serializable
    data class ExpenseDetails(val expenseId: Int) : Route

    @Serializable
    data class ExpenseAddEdit(val expenseId: Int? = null) : Route

    //Tasks
    @Serializable
    data object TaskList : Route

    @Serializable
    data object TaskDetails : Route

    @Serializable
    data object TaskAddEdit : Route

    //Calendar
    @Serializable
    data object Calendar : Route

    //More
    @Serializable
    data object More : Route

    //Fitness
    @Serializable
    data object FitnessDailyActivity : Route

    @Serializable
    data class FitnessActivityHistory(val fitnessRecordType: FitnessRecordType) : Route

    //Places
    @Serializable
    data object Places : Route

}