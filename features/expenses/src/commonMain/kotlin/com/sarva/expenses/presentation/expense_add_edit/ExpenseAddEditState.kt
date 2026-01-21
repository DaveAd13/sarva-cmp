package com.sarva.expenses.presentation.expense_add_edit

import androidx.compose.foundation.text.input.TextFieldState
import com.sarva.core.domain.model.ExpenseCategory
import kotlinx.datetime.LocalDateTime
import kotlin.random.Random

data class ExpenseAddEditState(
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val amountState: TextFieldState = TextFieldState(),
    val titleState: TextFieldState = TextFieldState(),
    val selectedCategory: ExpenseCategory? = null,
    val dateTime: LocalDateTime,
    val isDatePickerVisible: Boolean = false,
    val currency: String = "USD",
    val entries: List<ExpenseEntryState> = listOf(ExpenseEntryState()),
)

data class ExpenseEntryState(
    val name: TextFieldState = TextFieldState(),
    val amount: TextFieldState = TextFieldState(),
    val id: String = Random.nextInt().toString()
)