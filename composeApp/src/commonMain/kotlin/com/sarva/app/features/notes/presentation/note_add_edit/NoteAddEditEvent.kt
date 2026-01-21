package com.sarva.app.features.notes.presentation.note_add_edit

sealed interface NoteAddEditEvent {
    data object NavigateTo : NoteAddEditEvent
}