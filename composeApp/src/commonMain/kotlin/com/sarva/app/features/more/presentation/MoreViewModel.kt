package com.sarva.app.features.more.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class MoreViewModel : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MoreState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MoreState()
        )

    private val eventChannel = Channel<MoreEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: MoreAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}