package com.sarva.expenses.presentation.expense_add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.domain.model.ExpenseEntry
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.util.UiText
import com.sarva.expenses.domain.usecase.InsertExpenseUseCase
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.amount_cannot_be_empty
import com.sarva.features.expenses.generated.resources.failed_to_save_expense
import com.sarva.features.expenses.generated.resources.title_cannot_be_empty
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Clock

class ExpenseAddEditViewModel(
    private val expenseId: Int,
) : ViewModel(), KoinComponent {

    private val insertExpenseUseCase: InsertExpenseUseCase by inject()

    private val _state = MutableStateFlow(
        ExpenseAddEditState(
            dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    )
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ExpenseAddEditEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                isEditMode = expenseId != -1,
            )
        }
        if (expenseId != -1) {
            loadExpense()
        }
    }

    private fun loadExpense() {
        viewModelScope.launch {

        }
    }

    private fun validateAndSave() {
        val currentState = _state.value
        val title = currentState.titleState.text.toString().trim()
        val amount = currentState.amountState.text.toString().toDoubleOrNull()

        val errorMessage = when {
            amount == null -> UiText.StringRes(Res.string.amount_cannot_be_empty)
            title.isBlank() -> UiText.StringRes(Res.string.title_cannot_be_empty)
            else -> null
        }

        if (errorMessage != null) {
            viewModelScope.launch {
                eventChannel.send(ExpenseAddEditEvent.ShowSnackbar(errorMessage))
            }
            return
        }

        _state.update { it.copy(isLoading = true) }

        val expense = mapToDomain(currentState)
        saveExpense(expense)
    }

    private fun saveExpense(expense: Expense) {
        viewModelScope.launch {
            val result = insertExpenseUseCase(expense)
            when (result) {
                is Result.Success -> {
                    eventChannel.send(ExpenseAddEditEvent.ExpenseSaved)
                }

                is Result.Failure -> {
                    eventChannel.send(ExpenseAddEditEvent.ShowSnackbar(UiText.StringRes(Res.string.failed_to_save_expense)))
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun mapToDomain(state: ExpenseAddEditState): Expense {
        val entries = state.entries
            .filter { it.name.text.isNotBlank() && it.amount.text.isNotBlank() }
            .map { entry ->
                ExpenseEntry(
                    id = entry.id,
                    name = entry.name.text.toString(),
                    price = entry.amount.text.toString().toDouble()
                )
            }

        return Expense(
            title = state.titleState.text.toString().trim(),
            amount = state.amountState.text.toString().toDouble(),
            category = state.selectedCategory ?: ExpenseCategory.OTHER,
            currency = "USD",
            dateTime = state.dateTime,
            entries = entries
        )
    }

    private fun ensureTrailingEmptyRow() {
        _state.update { currentState ->
            val entries = currentState.entries
            val needsNewRow = entries.isEmpty() || entries.last().name.text.isNotBlank()

            if (needsNewRow) {
                currentState.copy(
                    entries = entries + ExpenseEntryState()
                )
            } else {
                currentState
            }
        }
    }

    fun onAction(action: ExpenseAddEditAction) {
        when (action) {
            ExpenseAddEditAction.OnSaveClicked -> {
                validateAndSave()
            }

            is ExpenseAddEditAction.OnCategoryClicked -> {
                _state.update {
                    it.copy(
                        selectedCategory = action.category
                    )
                }
            }

            ExpenseAddEditAction.OnDateClicked -> {
                _state.update {
                    it.copy(
                        isDatePickerVisible = true
                    )
                }
            }

            ExpenseAddEditAction.OnDatePickerDismissed -> {
                _state.update {
                    it.copy(
                        isDatePickerVisible = false
                    )
                }
            }

            is ExpenseAddEditAction.OnDateSelected -> {
                _state.update {
                    it.copy(
                        dateTime = action.dateTime,
                        isDatePickerVisible = false
                    )
                }
            }

            ExpenseAddEditAction.OnEntryChanged -> {
                ensureTrailingEmptyRow()
            }

            is ExpenseAddEditAction.OnEntryRemoved -> {
                _state.update { currentState ->
                    val updatedEntries = currentState.entries.filterIndexed { index, _ ->
                        index != action.index
                    }

                    currentState.copy(
                        entries = updatedEntries.ifEmpty { listOf(ExpenseEntryState()) }
                    )
                }
            }
        }
    }
}