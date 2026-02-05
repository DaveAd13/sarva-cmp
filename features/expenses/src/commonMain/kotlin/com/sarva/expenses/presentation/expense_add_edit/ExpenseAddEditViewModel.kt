package com.sarva.expenses.presentation.expense_add_edit

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.model.expense.ExpenseCategory
import com.sarva.core.domain.model.expense.ExpenseEntry
import com.sarva.core.domain.model.expense.ExpenseLocation
import com.sarva.core.domain.util.Resource
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.formatting.toPlainString
import com.sarva.core.presentation.util.UiText
import com.sarva.expenses.domain.usecase.GetExpenseUseCase
import com.sarva.expenses.domain.usecase.InsertExpenseUseCase
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.amount_cannot_be_empty
import com.sarva.features.expenses.generated.resources.failed_to_save_expense
import com.sarva.features.expenses.generated.resources.title_cannot_be_empty
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Clock

class ExpenseAddEditViewModel(
    private val expenseId: Int,
) : ViewModel(), KoinComponent {

    private val insertExpenseUseCase: InsertExpenseUseCase by inject()
    private val getExpenseUseCase: GetExpenseUseCase by inject()

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
                isEditMode = expenseId != 0,
            )
        }

        if (expenseId != 0) {
            loadExpense()
        }
    }

    private fun loadExpense() {
        getExpenseUseCase(expenseId)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        val expense = result.data
                        _state.value = mapFromDomain(expense)
                    }

                    is Resource.Failure -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                            )
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
//        saveMockExpenses()
    }

    private fun saveMockExpenses() {
        val mockExpenses = listOf(
            Expense(
                title = "Morning Coffee",
                category = ExpenseCategory.FOOD,
                amount = 1200.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2026-02-05T09:15:00"),
                location = ExpenseLocation(name = "Lumiere Coffee"),
                entries = listOf(ExpenseEntry(id = "e1", name = "Cappuccino", price = 1200.0))
            ),

            Expense(
                title = "Grocery Run",
                category = ExpenseCategory.GROCERIES,
                amount = 8500.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2026-02-04T18:30:00"),
                location = ExpenseLocation(name = "Carrefour", city = "Yerevan"),
                entries = listOf(
                    ExpenseEntry(id = "e2", name = "Fruit", price = 3000.0),
                    ExpenseEntry(id = "e3", name = "Dairy", price = 5500.0)
                )
            ),

            Expense(
                title = "Fuel",
                category = ExpenseCategory.TRANSPORT,
                amount = 15000.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2026-02-02T14:00:00"),
                location = ExpenseLocation(name = "Flash Gas"),
                entries = listOf(ExpenseEntry(id = "e4", name = "Gasoline", price = 15000.0))
            ),

            Expense(
                title = "Netflix Subscription",
                category = ExpenseCategory.ENTERTAINMENT,
                amount = 10.99,
                currency = "USD",
                dateTime = LocalDateTime.parse("2026-01-30T00:00:00"),
                entries = listOf(ExpenseEntry(id = "e5", name = "Monthly Plan", price = 10.99))
            ),

            Expense(
                title = "Dinner with Friends",
                category = ExpenseCategory.FOOD,
                amount = 32000.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2026-01-15T20:45:00"),
                location = ExpenseLocation(name = "Sherep", street = "Amiryan St"),
                entries = listOf(ExpenseEntry(id = "e6", name = "Dinner", price = 32000.0))
            ),

            Expense(
                title = "Gym Membership",
                category = ExpenseCategory.HEALTH,
                amount = 25000.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2026-01-05T10:00:00"),
                location = ExpenseLocation(name = "Gold's Gym"),
                entries = listOf(ExpenseEntry(id = "e7", name = "January Pass", price = 25000.0))
            ),

            Expense(
                title = "New Year Party",
                category = ExpenseCategory.FOOD,
                amount = 55000.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2025-12-31T23:30:00"),
                location = ExpenseLocation(name = "Republic Square"),
                entries = listOf(ExpenseEntry(id = "e8", name = "Celebration", price = 55000.0))
            ),

            Expense(
                title = "Amazon Tech Haul",
                category = ExpenseCategory.SHOPPING,
                amount = 145.50,
                currency = "USD",
                dateTime = LocalDateTime.parse("2025-12-20T15:30:00"),
                entries = listOf(
                    ExpenseEntry(id = "e9", name = "Mouse", price = 45.50),
                    ExpenseEntry(id = "e10", name = "Keyboard", price = 100.0)
                )
            ),

            Expense(
                title = "Electricity Bill",
                category = ExpenseCategory.BILLS,
                amount = 12000.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2025-11-10T11:00:00"),
                entries = emptyList()
            ),

            Expense(
                title = "Weekend in Dilijan",
                category = ExpenseCategory.TRAVEL,
                amount = 45000.0,
                currency = "AMD",
                dateTime = LocalDateTime.parse("2025-10-12T14:00:00"),
                location = ExpenseLocation(name = "Dilijan Park", city = "Dilijan"),
                entries = listOf(ExpenseEntry(id = "e11", name = "Hotel", price = 45000.0))
            )
        )

        mockExpenses.forEach { saveExpense(it) }
    }

    private fun saveExpense(expense: Expense) {
        viewModelScope.launch {
            when (insertExpenseUseCase(expense)) {
                is Result.Success -> {
                    if (expenseId == 0) {
                        eventChannel.send(ExpenseAddEditEvent.ExpenseSaved)
                    } else {
                        eventChannel.send(ExpenseAddEditEvent.ExpenseUpdated)
                    }
                }

                is Result.Failure -> {
                    eventChannel.send(ExpenseAddEditEvent.ShowSnackbar(UiText.StringRes(Res.string.failed_to_save_expense)))
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
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


            ExpenseAddEditAction.OnLocationCLicked -> {
                _state.update {
                    it.copy(
                        showLocationSearch = true
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

            ExpenseAddEditAction.OnLocationSearchDismissed -> {
                _state.update {
                    it.copy(
                        showLocationSearch = false
                    )
                }
            }

            is ExpenseAddEditAction.OnLocationSelected -> {
                val result = action.location
                val expenseLocation =  ExpenseLocation(
                    name = result.name,
                    country = result.country,
                    city = result.city,
                    street = result.street,
                    latitude = result.latitude,
                    longitude = result.longitude,
                )

                _state.update {
                    it.copy(
                        location = expenseLocation
                    )
                }
            }
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
            id = expenseId,
            title = state.titleState.text.toString().trim(),
            amount = state.amountState.text.toString().toDouble(),
            category = state.selectedCategory ?: ExpenseCategory.OTHER,
            currency = state.currency,
            location = state.location,
            dateTime = state.dateTime,
            entries = entries
        )
    }

    private fun mapFromDomain(expense: Expense): ExpenseAddEditState {
        val entryStates = if (expense.entries.isEmpty()) {
            listOf(ExpenseEntryState())
        } else {
            expense.entries.map { entry ->
                ExpenseEntryState(id = entry.id).apply {
                    name.setTextAndPlaceCursorAtEnd(entry.name)
                    amount.setTextAndPlaceCursorAtEnd(entry.price.toString())
                }
            }
        }

        return ExpenseAddEditState(
            isEditMode = true,
            selectedCategory = expense.category,
            dateTime = expense.dateTime,
            location = expense.location,
            currency = expense.currency,
            entries = entryStates
        ).apply {
            titleState.setTextAndPlaceCursorAtEnd(expense.title)
            amountState.setTextAndPlaceCursorAtEnd(expense.amount.toPlainString())
        }
    }
}
