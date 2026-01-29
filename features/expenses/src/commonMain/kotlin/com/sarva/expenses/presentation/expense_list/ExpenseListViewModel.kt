package com.sarva.expenses.presentation.expense_list

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.model.expense.ExpenseCategory
import com.sarva.core.domain.util.Resource
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.util.UiText
import com.sarva.expenses.domain.usecase.DeleteExpenseUseCase
import com.sarva.expenses.domain.usecase.GetExpensesUseCase
import com.sarva.expenses.domain.usecase.InsertExpenseUseCase
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.failed_to_save_expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExpenseListViewModel(
) : ViewModel(), KoinComponent {

    private val getExpensesUseCase: GetExpensesUseCase by inject()
    private val deleteExpenseUseCase: DeleteExpenseUseCase by inject()
    private val insertExpenseUseCase: InsertExpenseUseCase by inject()

    private val _state = MutableStateFlow(ExpenseListState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ExpenseListEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeExpenses()
            observeSearchQuery()
        }
    }

    private fun observeExpenses() {
        getExpensesUseCase()
            .map { result ->
                if (result is Resource.Success) {
                    val grouped = getFilteredAndGrouped(
                        allExpenses = result.data,
                        category = _state.value.selectedCategory,
                        query = _state.value.searchTextFieldState.text.toString()
                    )
                    result to grouped
                } else {
                    result to emptyMap()
                }
            }
            .flowOn(Dispatchers.Default)
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

    @OptIn(FlowPreview::class)
    private suspend fun observeSearchQuery() {
        snapshotFlow { _state.value.searchTextFieldState.text }
            .distinctUntilChanged()
            .debounce(300)
            .flowOn(Dispatchers.Default)
            .collect { text ->
                val grouped = getFilteredAndGrouped(
                    allExpenses = _state.value.expenses,
                    category = _state.value.selectedCategory,
                    query = text.toString()
                )

                _state.update {
                    it.copy(
                        groupedExpenses = grouped
                    )
                }
            }
    }

    private fun saveExpense(expense: Expense) {
        viewModelScope.launch {
            when (insertExpenseUseCase(expense)) {
                is Result.Success -> {

                }

                is Result.Failure -> {
                    eventChannel.send(ExpenseListEvent.ShowSnackbar(UiText.StringRes(Res.string.failed_to_save_expense)))
                }
            }
        }
    }

    private fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            val updatedList = _state.value.expenses.toMutableList().apply { remove(expense) }

            _state.update {
                it.copy(
                    expenses = updatedList,
                    groupedExpenses = groupExpenses(updatedList)
                )
            }

            when (deleteExpenseUseCase(expense.id)) {
                is Result.Failure -> {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }

                is Result.Success -> {
                    eventChannel.send(
                        ExpenseListEvent.ShowUndoSnackbar(expense = expense)
                    )
                }
            }
        }
    }

    private fun undoDelete(expense: Expense) {
        viewModelScope.launch {
            saveExpense(expense)
        }
    }

    private fun getFilteredAndGrouped(
        allExpenses: List<Expense>,
        category: ExpenseCategory?,
        query: String
    ): Map<String, List<Expense>> {
        val filtered = allExpenses.filter { expense ->
            val matchesCategory = category == null || expense.category == category
            val matchesSearch = query.isEmpty() ||
                    expense.title.contains(query, ignoreCase = true) ||
                    expense.location?.name?.contains(query, ignoreCase = true) == true

            matchesCategory && matchesSearch
        }
        return groupExpenses(filtered)
    }

    private fun groupExpenses(expenses: List<Expense>): Map<String, List<Expense>> {
        return expenses
            .sortedByDescending { it.dateTime }
            .groupBy { expense ->
                "${expense.dateTime.month.name} ${expense.dateTime.year}"
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

                val grouped = getFilteredAndGrouped(
                    allExpenses = _state.value.expenses,
                    category = action.category,
                    query = _state.value.searchTextFieldState.text.toString()
                )

                _state.update {
                    it.copy(
                        groupedExpenses = grouped
                    )
                }

                viewModelScope.launch {
                    yield()
                    eventChannel.send(ExpenseListEvent.ScrollToTUp)
                }
            }

            ExpenseListAction.ToggleSearch -> {
                val activating = !_state.value.isSearchActive

                _state.update {
                    it.copy(
                        isSearchActive = activating
                    )
                }

                if (!activating) {
                    _state.value.searchTextFieldState.clearText()
                }
            }

            is ExpenseListAction.DeleteExpense -> {
                deleteExpense(action.expense)
            }

            is ExpenseListAction.UndoDelete -> {
                undoDelete(action.expense)
            }
        }
    }
}