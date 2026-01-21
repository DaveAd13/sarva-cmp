package com.sarva.expenses.presentation.expense_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class ExpenseDetailsViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ExpenseDetailsState())
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
            initialValue = ExpenseDetailsState()
        )

    private val eventChannel = Channel<ExpenseDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ExpenseDetailsAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}