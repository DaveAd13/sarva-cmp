package com.sarva.expenses.presentation.expense_list

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.presentation.getIcon
import com.sarva.core.presentation.getLabel
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.NoDataView
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.core.presentation.util.ResultStore
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.designsystem.theme.sarvaShimmerTheme
import com.sarva.expenses.presentation.expense_list.components.CategoryChip
import com.sarva.expenses.presentation.expense_list.components.ExpenseCard
import com.sarva.expenses.presentation.expense_list.components.ExpenseCardShimmer
import com.sarva.expenses.presentation.expense_list.components.MonthHeaderShimmer
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.all
import com.sarva.features.expenses.generated.resources.expense_saved_successfully
import com.sarva.features.expenses.generated.resources.expenses
import com.sarva.features.expenses.generated.resources.failed_to_save_expense
import com.sarva.features.expenses.generated.resources.no_expenses_description
import com.sarva.features.expenses.generated.resources.no_expenses_title
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
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
    val scope = rememberCoroutineScope()

    val isSaved = resultStore.getResult<Boolean>("expense_saved")
    val isExpenseLoadingFailed = resultStore.getResult<Boolean>("expense_loading_failed")


    LaunchedEffect(isSaved) {
        if (isSaved == true) {
            snackbarHostState.showSnackbar(getString(Res.string.expense_saved_successfully))
            resultStore.removeResult("expense_saved")
        }
        if (isExpenseLoadingFailed == true) {
            snackbarHostState.showSnackbar(getString(Res.string.failed_to_save_expense))
            resultStore.removeResult("expense_loading_failed")
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ExpenseListEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message.asString())
                }
            }
        }
    }

    ExpenseListScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
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
    onExpenseClick: (Int) -> Unit,
    onAddExpenseClick: () -> Unit,
    onBack: () -> Unit
) {
    val containerColor = SarvaTheme.colors.expenseContainer
    val contentColor = SarvaTheme.colors.expenseContent
    val cardContainerColor = SarvaTheme.colors.expenseCardContainer
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = containerColor,
        topBar = {
            Column(modifier = Modifier.background(containerColor)) {
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
                        IconButton(onClick = {}) {
                            Icon(Icons.Rounded.Search, null)
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Rounded.BarChart, null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = containerColor,
                        scrolledContainerColor = containerColor,
                        titleContentColor = contentColor,
                        navigationIconContentColor = contentColor,
                        actionIconContentColor = contentColor
                    ),
                    scrollBehavior = scrollBehavior
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
        val layoutDirection = LocalLayoutDirection.current
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
                modifier = Modifier.fillMaxSize()
                    .background(containerColor),
                contentPadding = PaddingValues(
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection),
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding() + 16.dp
                ),
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
                    //TODO: FIX RECOMPOSITIONS
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
                                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
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
            }
        }
    }
}

@Preview(
    name = "Light",
)
@Preview(
    name = "Dark",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
)
@Composable
private fun Preview() {
    SarvaTheme {
        ExpenseListScreen(
            state = ExpenseListState(),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
            onExpenseClick = {},
            onAddExpenseClick = {},
            onBack = {}
        )
    }
}