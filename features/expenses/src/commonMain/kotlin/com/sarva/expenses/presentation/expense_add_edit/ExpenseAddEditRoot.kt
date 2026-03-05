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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.domain.expenses.model.ExpenseCategory
import com.sarva.core.presentation.currency_picker.presentation.CurrencyPickerRoot
import com.sarva.core.presentation.formatting.formatToShortDisplay
import com.sarva.core.presentation.getIcon
import com.sarva.core.presentation.getLabel
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.core.presentation.util.ResultStore
import com.sarva.core.presentation.util.fadingEdges
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
import com.sarva.features.expenses.generated.resources.empty
import com.sarva.features.expenses.generated.resources.item_name
import com.sarva.features.expenses.generated.resources.location
import com.sarva.features.expenses.generated.resources.ok
import com.sarva.features.expenses.generated.resources.total_amount
import com.sarva.features.expenses.generated.resources.what_is_it_for
import com.sarva.features.location.presentation.LocationSearchRoot
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Instant

@Composable
fun ExpenseAddEditRoot(
    expenseId: Int? = null,
    resultStore: ResultStore,
    viewModel: ExpenseAddEditViewModel = koinViewModel { parametersOf(expenseId ?: 0) },
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

            ExpenseAddEditEvent.ExpenseUpdated -> {
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
    val accentColor = SarvaTheme.colors.expenses
    val focusRequester = remember { FocusRequester() }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        selectionColors = TextSelectionColors(
            handleColor = accentColor,
            backgroundColor = accentColor.copy(alpha = 0.2f)
        ),
        cursorColor = accentColor,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
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
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(ExpenseAddEditAction.OnSaveClicked) },
                modifier = Modifier.imePadding(),
                shape = CircleShape,
                containerColor = accentColor,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
            }
        }
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
                    accentColor = accentColor,
                    textFieldColors = textFieldColors,
                    focusRequester = focusRequester
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(
                                start = 14.dp,
                                top = 16.dp,
                                end = 14.dp,
                                bottom = 8.dp
                            ),
                            text = stringResource(Res.string.breakdown),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                            ),
                        )

                        state.entries.forEachIndexed { index, entry ->
                            Column {
                                BreakdownEntryRow(
                                    index = index,
                                    entry = entry,
                                    accentColor = accentColor,
                                    onChange = { onAction(ExpenseAddEditAction.OnEntryChanged) },
                                    onRemove = { onAction(ExpenseAddEditAction.OnEntryRemoved(index)) }
                                )

                                if (index < state.entries.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 14.dp), // Optional: inset dividers
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
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
            accentColor = accentColor
        )
    }

    if (state.isCurrencyPickerVisible) {
        CurrencyPickerRoot(
            onCurrencySelected = { currency ->
                onAction(ExpenseAddEditAction.OnCurrencySelected(currency))
            },
            onDismiss = { onAction(ExpenseAddEditAction.OnCurrencyPickerDismissed) },
            accentColor = accentColor
        )
    }

    if (state.isLocationSearchVisible) {
        LocationSearchRoot(
            onLocationSelected = { location ->
                onAction(ExpenseAddEditAction.OnLocationSelected(location))
            },
            onDismiss = { onAction(ExpenseAddEditAction.OnLocationSearchDismissed) },
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
    accentColor: Color,
    textFieldColors: TextFieldColors,
    focusRequester: FocusRequester
) {
    val rowState = rememberLazyListState()

    LaunchedEffect(state.selectedCategory) {
        val category = state.selectedCategory ?: return@LaunchedEffect
        val index = ExpenseCategory.entries.indexOf(category)

        if (index >= 0) {
            snapshotFlow { rowState.layoutInfo.totalItemsCount }
                .filter { it > index }
                .first()

            val viewportWidth = rowState.layoutInfo.viewportSize.width
            val itemWidth = rowState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
            val centerOffset = (viewportWidth - itemWidth) / 2

            rowState.animateScrollToItem(
                index = index,
                scrollOffset = -centerOffset
            )
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 14.dp, top = 8.dp, end = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onAction(ExpenseAddEditAction.OnCurrencyClicked) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = state.currency,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                TextField(
                    state = state.amountState,
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    inputTransformation = CurrencyInputTransformation,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.total_amount),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
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
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            TextField(
                state = state.titleState,
                modifier = Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp),
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.what_is_it_for),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
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
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fadingEdges(
                        listState = rowState,
                        contentPadding = PaddingValues(14.dp),
                    ),
                state = rowState,
                contentPadding = PaddingValues(14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(ExpenseCategory.entries) { category ->
                    CategoryChip(
                        label = category.getLabel().asStringC(),
                        categoryIcon = category.getIcon(),
                        onClick = { onAction(ExpenseAddEditAction.OnCategoryClicked(category)) },
                        clickable = !state.isLoading,
                        isSelected = state.selectedCategory == category,
                        accentColor = accentColor
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                    ),
                )

                Text(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                    text = state.dateTime.formatToShortDisplay(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                    ),
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onAction(ExpenseAddEditAction.OnLocationCLicked) }),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                    text = stringResource(Res.string.location),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                    ),
                )

                Text(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                    text = state.location?.name ?: stringResource(Res.string.empty),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
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
    accentColor: Color,
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )

        LaunchedEffect(entry.name.text) {
            if (entry.name.text.isNotEmpty()) {
                onChange()
            }
        }

        val customSelectionColors = TextSelectionColors(
            handleColor = accentColor,
            backgroundColor = accentColor.copy(alpha = 0.2f)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
            BasicTextField(
                state = entry.name,
                modifier = Modifier.weight(1f),
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                cursorBrush = SolidColor(accentColor),
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (entry.name.text.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.item_name),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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

            BasicTextField(
                state = entry.amount,
                modifier = Modifier.weight(1f),
                lineLimits = TextFieldLineLimits.SingleLine,
                inputTransformation = CurrencyInputTransformation,
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                cursorBrush = SolidColor(accentColor),
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (entry.amount.text.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.amount),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
    accentColor: Color
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateTime.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds(),
    )

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = accentColor,
            onPrimary = MaterialTheme.colorScheme.surface
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
                        color = accentColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ExpenseAddEditAction.OnDatePickerDismissed) }) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedDayContainerColor = accentColor,
                    selectedDayContentColor = MaterialTheme.colorScheme.surface,
                    todayContentColor = accentColor,
                    todayDateBorderColor = accentColor
                )
            )
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        ExpenseAddEditScreen(
            state = ExpenseAddEditState(
                dateTime = LocalDateTime.parse("2024-09-10T00:00:00")
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
            onBack = {}
        )
    }
}