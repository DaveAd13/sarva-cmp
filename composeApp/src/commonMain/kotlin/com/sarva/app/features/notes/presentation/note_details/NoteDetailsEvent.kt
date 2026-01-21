package com.sarva.app.features.notes.presentation.note_details

sealed interface NoteDetailsEvent {
    data object NavigateTo : NoteDetailsEvent
}