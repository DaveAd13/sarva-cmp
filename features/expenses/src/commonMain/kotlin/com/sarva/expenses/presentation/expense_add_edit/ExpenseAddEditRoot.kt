package com.sarva.expenses.presentation.expense_add_edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.presentation.getIcon
import com.sarva.core.presentation.getLabel
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.core.presentation.util.ResultStore
import com.sarva.core.presentation.util.formatToDisplay
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.expenses.presentation.expense_add_edit.components.CurrencyInputTransformation
import com.sarva.expenses.presentation.expense_list.components.CategoryChip
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.add_expense
import com.sarva.features.expenses.generated.resources.amount
import com.sarva.features.expenses.generated.resources.breakdown
import com.sarva.features.expenses.generated.resources.cancel
import com.sarva.features.expenses.generated.resources.date
import com.sarva.features.expenses.generated.resources.edit_expense
import com.sarva.features.expenses.generated.resources.item_name
import com.sarva.features.expenses.generated.resources.ok
import com.sarva.features.expenses.generated.resources.total_amount
import com.sarva.features.expenses.generated.resources.what_is_it_for
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun ExpenseAddEditRoot(
    expenseId: Int? = null,
    resultStore: ResultStore,
    viewModel: ExpenseAddEditViewModel = koinViewModel { parametersOf(expenseId ?: -1) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onBack = LocalBackHandler.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ExpenseAddEditEvent.ExpenseSaved -> {
                resultStore.setResult("expense_saved", true)
                onBack()
            }

            is ExpenseAddEditEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message.asString())
                }
            }
        }
    }

    ExpenseAddEditScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddEditScreen(
    state: ExpenseAddEditState,
    snackbarHostState: SnackbarHostState,
    onAction: (ExpenseAddEditAction) -> Unit,
    onBack: () -> Unit
) {
    val containerColor = SarvaTheme.colors.expenseContainer
    val contentColor = SarvaTheme.colors.expenseContent
    val cardContainerColor = SarvaTheme.colors.expenseCardContainer
    val focusRequester = remember { FocusRequester() }
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        selectionColors = TextSelectionColors(
            handleColor = contentColor,
            backgroundColor = contentColor.copy(alpha = 0.2f)
        ),
        cursorColor = contentColor,
    )
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(if (state.isEditMode) Res.string.edit_expense else Res.string.add_expense),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerColor,
                    scrolledContainerColor = containerColor,
                    titleContentColor = contentColor,
                    navigationIconContentColor = contentColor,
                    actionIconContentColor = contentColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(ExpenseAddEditAction.OnSaveClicked) },
                modifier = Modifier.imePadding(),
                shape = CircleShape,
                containerColor = contentColor,
                contentColor = containerColor
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
            }
        },
        containerColor = containerColor
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .imePadding(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 0.dp,
                end = 16.dp,
                bottom = 80.dp
            )
        ) {
            item {
                MainExpenseCard(
                    state = state,
                    onAction = onAction,
                    contentColor = contentColor,
                    cardContainerColor = cardContainerColor,
                    textFieldColors = textFieldColors,
                    focusRequester = focusRequester
                )
            }

            item {
                val shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)

                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    shape = shape,
                    color = cardContainerColor
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 14.dp,
                            top = 16.dp,
                            end = 14.dp,
                            bottom = 8.dp
                        ),
                        text = stringResource(Res.string.breakdown),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = contentColor.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
            }

            itemsIndexed(
                items = state.entries,
                key = { _, entry -> entry.id }
            ) { index, entry ->
                val (shape, bottomPadding) = when {
                    index == state.entries.lastIndex -> RoundedCornerShape(
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    ) to 8.dp

                    else -> RectangleShape to 0.dp
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = shape,
                    color = cardContainerColor
                ) {
                    Column(modifier = Modifier.padding(bottom = bottomPadding)) {
                        BreakdownEntryRow(
                            index = index,
                            entry = entry,
                            contentColor = contentColor,
                            onChange = { onAction(ExpenseAddEditAction.OnEntryChanged) },
                            onRemove = { onAction(ExpenseAddEditAction.OnEntryRemoved(index)) }
                        )

                        if (index < state.entries.lastIndex) {
                            HorizontalDivider(
//                                modifier = Modifier.padding(horizontal = 14.dp),
                                color = contentColor.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.isDatePickerVisible) {
        ExpenseDatePicker(
            dateTime = state.dateTime,
            onAction = onAction,
            containerColor = containerColor,
            contentColor = contentColor
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun MainExpenseCard(
    state: ExpenseAddEditState,
    onAction: (ExpenseAddEditAction) -> Unit,
    contentColor: Color,
    cardContainerColor: Color,
    textFieldColors: TextFieldColors,
    focusRequester: FocusRequester
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 14.dp, top = 8.dp, end = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {},
                    contentPadding = PaddingValues(
                        horizontal = 4.dp,
                        vertical = 2.dp
                    ),
                    border = BorderStroke(1.dp, contentColor.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = state.currency,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = contentColor,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    state = state.amountState,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    inputTransformation = CurrencyInputTransformation,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.total_amount),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = contentColor.copy(alpha = 0.5f),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = contentColor,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    colors = textFieldColors,
                    contentPadding = PaddingValues(0.dp),
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                color = contentColor.copy(alpha = 0.1f)
            )
            TextField(
                modifier = Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp),
                state = state.titleState,
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.what_is_it_for),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = contentColor.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                colors = textFieldColors,
                contentPadding = PaddingValues(0.dp),
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                color = contentColor.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(ExpenseCategory.entries) { category ->
                    CategoryChip(
                        label = category.getLabel().asStringC(),
                        categoryIcon = category.getIcon(),
                        onClick = { onAction(ExpenseAddEditAction.OnCategoryClicked(category)) },
                        clickable = !state.isLoading,
                        isSelected = state.selectedCategory == category,
                        shape = FilterChipDefaults.shape,
                        containerColor = Color.Transparent,
                        selectedContainerColor = contentColor,
                        contentColor = contentColor,
                        selectedContentColor = cardContainerColor,
                        iconContainerColor = Color.Transparent,
                        borderColor = contentColor.copy(alpha = 0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                color = contentColor.copy(alpha = 0.1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onAction(ExpenseAddEditAction.OnDateClicked) }),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                    text = stringResource(Res.string.date),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = contentColor.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                    ),
                )

                Text(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                    text = state.dateTime.formatToDisplay(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = contentColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                    ),
                )
            }
        }
    }
}

@Composable
private fun BreakdownEntryRow(
    index: Int,
    entry: ExpenseEntryState,
    contentColor: Color,
    onChange: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${(index + 1)}.",
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                color = contentColor.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        )

        LaunchedEffect(entry.name.text) {
            if (entry.name.text.isNotEmpty()) {
                onChange()
            }
        }

        val customSelectionColors = TextSelectionColors(
            handleColor = contentColor,
            backgroundColor = contentColor.copy(alpha = 0.2f)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
            BasicTextField(
                state = entry.name,
                modifier = Modifier.weight(1f),
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                cursorBrush = SolidColor(contentColor),
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (entry.name.text.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.item_name),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = contentColor.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Start
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(10.dp))

//            VerticalDivider(
//                modifier = Modifier
//                    .height(16.dp)
//                    .padding(horizontal = 8.dp),
//                thickness = 1.dp,
//                color = contentColor.copy(alpha = 0.1f)
//            )

            BasicTextField(
                state = entry.amount,
                modifier = Modifier.weight(1f),
                lineLimits = TextFieldLineLimits.SingleLine,
                inputTransformation = CurrencyInputTransformation,
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                cursorBrush = SolidColor(contentColor),
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (entry.amount.text.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.amount),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = contentColor.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.End
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = null,
                    tint = contentColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ExpenseDatePicker(
    dateTime: LocalDateTime,
    onAction: (ExpenseAddEditAction) -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateTime.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds(),
    )

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = contentColor,
        )
    ) {
        @OptIn(ExperimentalMaterial3Api::class)
        DatePickerDialog(
            onDismissRequest = { onAction(ExpenseAddEditAction.OnDatePickerDismissed) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC)
                        onAction(ExpenseAddEditAction.OnDateSelected(localDate))
                    }
                }) {
                    Text(
                        text = stringResource(Res.string.ok),
                        color = contentColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ExpenseAddEditAction.OnDatePickerDismissed) }) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        color = contentColor
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = containerColor,
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = containerColor,
                )
            )
        }
    }
}

@Preview
@Composable
private fun LightPreview() {
    SarvaTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExpenseAddEditScreen(
                state = ExpenseAddEditState(
                    dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                ),
                snackbarHostState = remember { SnackbarHostState() },
                onAction = {},
                onBack = {}
            )
        }
    }
}

@Preview
@Composable
private fun DarkPreview() {
    SarvaTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExpenseAddEditScreen(
                state = ExpenseAddEditState(
                    dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                ),
                snackbarHostState = remember { SnackbarHostState() },
                onAction = {},
                onBack = {}
            )
        }
    }
}