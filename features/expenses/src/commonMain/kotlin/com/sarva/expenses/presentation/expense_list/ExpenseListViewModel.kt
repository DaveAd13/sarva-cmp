package com.sarva.expenses.presentation.expense_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.util.Resource
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.util.ResultStore
import com.sarva.expenses.domain.usecase.DeleteExpenseUseCase
import com.sarva.expenses.domain.usecase.GetExpensesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExpenseListViewModel(
) : ViewModel(), KoinComponent {

    private val getExpensesUseCase: GetExpensesUseCase by inject()
    private val deleteExpenseUseCase: DeleteExpenseUseCase by inject()

    private val _state = MutableStateFlow(ExpenseListState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ExpenseListEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            observeExpenses()
        }
    }

    private fun observeExpenses() {
        getExpensesUseCase()
            .flowOn(Dispatchers.IO)
            .map { result ->
                if (result is Resource.Success) {
                    withContext(Dispatchers.Default) {
                        val grouped = groupExpenses(result.data)
                        result to grouped
                    }
                } else {
                    result to emptyMap()
                }
            }
            .onEach { (result, grouped) ->
                _state.update {
                    it.copy(
                        isLoading = result is Resource.Loading,
                        expenses = (result as? Resource.Success)?.data ?: it.expenses,
                        groupedExpenses = grouped
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun deleteExpense(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when(deleteExpenseUseCase(id)) {
                is Result.Failure -> {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
                is Result.Success -> {
//                    TODO("Show snackbar with UNDO action")
                }
            }

        }
    }

//    private fun observeExpenses() {
//        getExpensesUseCase()
//            .onEach { result ->
//                when (result) {
//                    is Resource.Loading -> {
//                        _state.update {
//                            it.copy(isLoading = true)
//                        }
//                    }
//
//                    is Resource.Failure -> {
//                        _state.update {
//                            it.copy(isLoading = false)
//                        }
//                    }
//
//                    is Resource.Success -> {
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                expenses = result.data
//                            )
//                        }
//                        applyFilterAndGroup()
//                    }
//                }
//
//            }.launchIn(viewModelScope)
//    }

    private fun applyFilterAndGroup() {
        val currentState = _state.value
        val filtered = if (currentState.selectedCategory == null) {
            currentState.expenses
        } else {
            currentState.expenses.filter { it.category == currentState.selectedCategory }
        }

        _state.update { it.copy(groupedExpenses = groupExpenses(filtered)) }
    }

    private fun groupExpenses(expenses: List<Expense>): Map<String, List<Expense>> {
        return expenses
            .sortedByDescending { it.dateTime }
            .groupBy { expense ->
                val monthName =
                    expense.dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
                "$monthName ${expense.dateTime.year}"
            }
    }

    fun onAction(action: ExpenseListAction) {
        when (action) {
            is ExpenseListAction.CategoryClicked -> {
                _state.update {
                    it.copy(
                        selectedCategory = action.category
                    )
                }
                applyFilterAndGroup()
            }

            is ExpenseListAction.ExpenseClicked -> {
                deleteExpense(action.expenseId)
            }
        }
    }
}