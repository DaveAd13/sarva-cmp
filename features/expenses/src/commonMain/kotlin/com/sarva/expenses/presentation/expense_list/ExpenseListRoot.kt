package com.sarva.expenses.presentation.expense_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.domain.expenses.model.ExpenseCategory
import com.sarva.core.presentation.getIcon
import com.sarva.core.presentation.getLabel
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.NoDataView
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.core.presentation.util.ResultStore
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.designsystem.theme.sarvaShimmerTheme
import com.sarva.expenses.presentation.expense_list.ExpenseListAction.UndoDelete
import com.sarva.expenses.presentation.expense_list.components.CategoryChip
import com.sarva.expenses.presentation.expense_list.components.ExpenseCard
import com.sarva.expenses.presentation.expense_list.components.ExpenseCardShimmer
import com.sarva.expenses.presentation.expense_list.components.MonthHeaderShimmer
import com.sarva.expenses.presentation.expense_list.components.SearchTopBar
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.all
import com.sarva.features.expenses.generated.resources.expense_deleted_successfully
import com.sarva.features.expenses.generated.resources.expense_saved_successfully
import com.sarva.features.expenses.generated.resources.expenses
import com.sarva.features.expenses.generated.resources.failed_to_load_expense
import com.sarva.features.expenses.generated.resources.no_expenses_description
import com.sarva.features.expenses.generated.resources.no_expenses_title
import com.sarva.features.expenses.generated.resources.undo
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExpenseListRoot(
    resultStore: ResultStore,
    viewModel: ExpenseListViewModel = koinViewModel(),
    onExpenseClick: (Int) -> Unit,
    onAddExpenseClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onBack = LocalBackHandler.current

    val snackbarHostState = remember { SnackbarHostState() }
    val isSaved = resultStore.getResult<Boolean>("expense_saved")
    val isExpenseLoadingFailed = resultStore.getResult<Boolean>("expense_loading_failed")

    LaunchedEffect(isSaved) {
        if (isSaved == true) {
            resultStore.removeResult("expense_saved")
            snackbarHostState.showSnackbar(getString(Res.string.expense_saved_successfully))
        }
        if (isExpenseLoadingFailed == true) {
            resultStore.removeResult("expense_loading_failed")
            snackbarHostState.showSnackbar(getString(Res.string.failed_to_load_expense))
        }
    }

    ExpenseListScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        events = viewModel.events,
        onExpenseClick = onExpenseClick,
        onAddExpenseClick = onAddExpenseClick,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    state: ExpenseListState,
    snackbarHostState: SnackbarHostState,
    onAction: (ExpenseListAction) -> Unit,
    events: Flow<ExpenseListEvent>,
    onExpenseClick: (Int) -> Unit,
    onAddExpenseClick: () -> Unit,
    onBack: () -> Unit
) {
    val containerColor = SarvaTheme.colors.expenseContainer
    val contentColor = SarvaTheme.colors.expenseContent
    val cardContainerColor = SarvaTheme.colors.expenseCardContainer
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val hazeState = rememberHazeState()
    val isScrolled by remember {
        derivedStateOf { scrollBehavior.state.contentOffset < -1f }
    }

    ObserveAsEvents(events) { event ->
        when (event) {
            is ExpenseListEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message.asString())
                }
            }

            is ExpenseListEvent.ShowUndoSnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = getString(Res.string.expense_deleted_successfully),
                        actionLabel = getString(Res.string.undo),
                        duration = SnackbarDuration.Short
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            onAction(UndoDelete(event.expense))
                        }

                        SnackbarResult.Dismissed -> {

                        }
                    }
                }
            }

            ExpenseListEvent.ScrollToTUp -> {
                scope.launch {
                    listState.scrollToItem(0)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = containerColor,

        topBar = {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .hazeEffect(hazeState) {
                        blurRadius = 15.dp
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = if (isScrolled) 1f else 0f,
                            endIntensity = if (isScrolled) 0.3f else 0f,
                        )
                    },
            ) {
                AnimatedContent(
                    targetState = state.isSearchActive,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "TopAppBarAnimation"
                ) { isSearching ->
                    if (isSearching) {
                        SearchTopBar(
                            state = state.searchTextFieldState,
                            onCancelSearch = { onAction(ExpenseListAction.ToggleSearch) }
                        )
                    } else {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = stringResource(Res.string.expenses),
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
                            actions = {
                                IconButton(onClick = { onAction(ExpenseListAction.ToggleSearch) }) {
                                    Icon(Icons.Rounded.Search, null)
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Rounded.BarChart, null)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                scrolledContainerColor = Color.Transparent,
                                titleContentColor = contentColor,
                                navigationIconContentColor = contentColor,
                                actionIconContentColor = contentColor
                            ),
                            scrollBehavior = scrollBehavior
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .background(
//                            brush = Brush.verticalGradient(
//                                0.0f to containerColor,
//                                0.65f to containerColor,
//                                1.0f to Color.Transparent
//                            )
//                        ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        CategoryChip(
                            label = stringResource(Res.string.all),
                            onClick = { onAction(ExpenseListAction.CategoryClicked(null)) },
                            clickable = !state.isLoading,
                            isSelected = state.selectedCategory == null,
                            shape = CircleShape,
                            containerColor = cardContainerColor,
                            selectedContainerColor = contentColor,
                            contentColor = contentColor,
                            selectedContentColor = cardContainerColor,
                            iconContainerColor = containerColor,
                            borderColor = Color.Transparent
                        )
                    }

                    items(ExpenseCategory.entries) { category ->
                        CategoryChip(
                            label = category.getLabel().asStringC(),
                            categoryIcon = category.getIcon(),
                            onClick = { onAction(ExpenseListAction.CategoryClicked(category)) },
                            clickable = !state.isLoading,
                            isSelected = state.selectedCategory == category,
                            shape = CircleShape,
                            containerColor = cardContainerColor,
                            selectedContainerColor = contentColor,
                            contentColor = contentColor,
                            selectedContentColor = cardContainerColor,
                            iconContainerColor = containerColor,
                            borderColor = Color.Transparent
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddExpenseClick() },
                shape = CircleShape,
                containerColor = contentColor,
                contentColor = containerColor
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { contentPadding ->
        if (!state.isLoading && state.expenses.isEmpty()) {
            NoDataView(
                title = stringResource(Res.string.no_expenses_title),
                description = stringResource(Res.string.no_expenses_description),
                icon = Icons.AutoMirrored.Rounded.ReceiptLong,
                color = contentColor,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            )
        } else {
            val shimmerInstance = rememberShimmer(
                shimmerBounds = ShimmerBounds.Window,
                theme = sarvaShimmerTheme
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(state = hazeState)
                    .background(containerColor),
                state = listState,
                contentPadding = contentPadding
            ) {
                if (state.isLoading) {
                    item {
                        MonthHeaderShimmer(shimmerInstance)
                    }
                    items(15) {
                        ExpenseCardShimmer(shimmerInstance)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                } else {
                    state.groupedExpenses.forEach { (monthHeader, expensesInMonth) ->
                        item(
                            key = "header_$monthHeader",
                            contentType = { "header" }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(containerColor)
                                    .animateItem(
                                        fadeInSpec = tween(durationMillis = 300),
                                        fadeOutSpec = tween(durationMillis = 300),
                                        placementSpec = spring(stiffness = Spring.StiffnessLow)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = monthHeader.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        letterSpacing = 1.2.sp
                                    ),
                                    color = contentColor.copy(alpha = 0.5f)
                                )
                            }
                        }

                        items(
                            items = expensesInMonth,
                            key = { it.id },
                            contentType = { "expense_card" }
                        ) { expense ->
                            ExpenseCard(
                                expense = expense,
                                onClick = { onExpenseClick(expense.id) },
                                onDelete = { onAction(ExpenseListAction.DeleteExpense(expense)) },
                                cardContainerColor = cardContainerColor,
                                contentColor = contentColor,
                                containerColor = containerColor,
                                modifier = Modifier.animateItem(
                                    fadeInSpec = tween(durationMillis = 300),
                                    fadeOutSpec = tween(durationMillis = 300),
                                    placementSpec = spring(stiffness = Spring.StiffnessLow)
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        ExpenseListScreen(
            state = ExpenseListState(),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
            events = emptyFlow(),
            onExpenseClick = {},
            onAddExpenseClick = {},
            onBack = {}
        )
    }
}