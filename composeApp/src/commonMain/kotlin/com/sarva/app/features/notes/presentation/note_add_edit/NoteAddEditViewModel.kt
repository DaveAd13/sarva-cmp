package com.sarva.app.features.notes.presentation.note_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class NoteAddEditViewModel(
    noteId: String? = null,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(NoteAddEditState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<NoteAddEditEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: NoteAddEditAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}