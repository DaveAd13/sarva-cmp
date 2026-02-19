package com.sarva.expenses.presentation.expense_add_edit

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.sarva.core.domain.model.expense.ExpenseCategory
import com.sarva.core.domain.model.expense.ExpenseLocation
import kotlinx.datetime.LocalDateTime
import kotlin.random.Random

@Immutable
data class ExpenseAddEditState(
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val amountState: TextFieldState = TextFieldState(),
    val titleState: TextFieldState = TextFieldState(),
    val selectedCategory: ExpenseCategory? = null,
    val dateTime: LocalDateTime,
    val location: ExpenseLocation? = null,
    val isCurrencyPickerVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    val isLocationSearchVisible: Boolean = false,
    val currency: String = "USD",
    val entries: List<ExpenseEntryState> = listOf(ExpenseEntryState()),
)

data class ExpenseEntryState(
    val name: TextFieldState = TextFieldState(),
    val amount: TextFieldState = TextFieldState(),
    val id: String = Random.nextInt().toString()
)