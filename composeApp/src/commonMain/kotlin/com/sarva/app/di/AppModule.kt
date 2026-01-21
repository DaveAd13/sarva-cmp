package com.sarva.app.di

import com.sarva.app.features.calendar.presentation.CalendarViewModel
import com.sarva.app.features.home.presentation.HomeViewModel
import com.sarva.app.features.notes.presentation.note_add_edit.NoteAddEditViewModel
import com.sarva.app.features.notes.presentation.note_details.NoteDetailsViewModel
import com.sarva.app.features.notes.presentation.note_list.NoteListViewModel
import com.sarva.app.features.places.presentation.PlacesViewModel
import com.sarva.app.features.profile.presentation.ProfileViewModel
import com.sarva.app.features.tasks.presentation.task_list.TaskListViewModel
import com.sarva.core.presentation.snackbars.SnackbarManager
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    viewModelOf(::HomeViewModel)

    viewModelOf(::NoteListViewModel)
    viewModelOf(::NoteAddEditViewModel)
    viewModelOf(::NoteDetailsViewModel)

    viewModelOf(::TaskListViewModel)

    viewModelOf(::PlacesViewModel)

    viewModelOf(::CalendarViewModel)

    viewModelOf(::ProfileViewModel)

    singleOf(::SnackbarManager)
}