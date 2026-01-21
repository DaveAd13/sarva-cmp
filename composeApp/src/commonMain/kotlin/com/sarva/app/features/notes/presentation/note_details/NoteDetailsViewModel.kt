package com.sarva.app.features.notes.presentation.note_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    noteId: String,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<NoteDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadNote(noteId)
    }

    private fun loadNote(id: String) {
        viewModelScope.launch {
           TODO()
        }
    }

    fun onAction(action: NoteDetailsAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}