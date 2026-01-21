package com.sarva.app.features.notes.presentation.note_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class NoteListViewModel : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<NoteListEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: NoteListAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}