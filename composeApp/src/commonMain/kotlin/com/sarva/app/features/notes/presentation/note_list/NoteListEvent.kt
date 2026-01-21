package com.sarva.app.features.notes.presentation.note_list

sealed interface NoteListEvent {
    data object NavigateTo : NoteListEvent
}