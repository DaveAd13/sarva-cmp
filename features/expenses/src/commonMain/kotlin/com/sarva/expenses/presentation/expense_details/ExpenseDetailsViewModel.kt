package com.sarva.expenses.presentation.expense_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.util.Resource
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.util.UiText
import com.sarva.expenses.domain.usecase.DeleteExpenseUseCase
import com.sarva.expenses.domain.usecase.GetExpenseUseCase
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.failed_to_delete_expense
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpenseDetailsViewModel(
    private val expenseId: Int,
    private val getExpenseUseCase: GetExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseDetailsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ExpenseDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        observeExpense()
    }


    private fun observeExpense() {
        getExpenseUseCase(expenseId)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                expense = result.data
                            )
                        }
                    }

                    is Resource.Failure -> {
                        when (result.throwable) {
                            is NoSuchElementException -> {
                                eventChannel.send(ExpenseDetailsEvent.ExpenseDeleted)
                            }

                            else -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        expense = null
                                    )
                                }
                                eventChannel.send(ExpenseDetailsEvent.ExpenseLoadingFailed)
                            }
                        }
                    }

                    Resource.Loading -> _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun deleteExpense() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            when (deleteExpenseUseCase(expenseId)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }

                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    eventChannel.send(ExpenseDetailsEvent.ShowSnackbar(UiText.StringRes(Res.string.failed_to_delete_expense)))
                }
            }
        }
    }

    fun onAction(action: ExpenseDetailsAction) {
        when (action) {
            ExpenseDetailsAction.OnEditClicked -> {
                viewModelScope.launch {
                    eventChannel.send(ExpenseDetailsEvent.OnEditClicked)
                }
            }

            ExpenseDetailsAction.OnDeleteClicked -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = true
                    )
                }
            }

            ExpenseDetailsAction.OnDeleteConfirmed -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }
                deleteExpense()
            }

            ExpenseDetailsAction.OnDeleteCancelled -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }
            }
        }
    }
}