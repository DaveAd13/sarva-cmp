package com.sarva.expenses.presentation.expense_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.util.Result
import com.sarva.expenses.domain.usecase.GetExpenseUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpenseDetailsViewModel(
    private val expenseId: Int,
    private val getExpenseUseCase: GetExpenseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseDetailsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ExpenseDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadExpense()
    }

    private fun loadExpense() {
        viewModelScope.launch {
            when (val result = getExpenseUseCase(expenseId)) {
                is Result.Success ->  {
                    _state.update {
                        it.copy(
                            expense = result.data
                        )
                    }
                }
                is Result.Failure -> {
                    eventChannel.send(ExpenseDetailsEvent.ExpenseLoadingFailed)
                }
            }

        }
    }

    fun onAction(action: ExpenseDetailsAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}