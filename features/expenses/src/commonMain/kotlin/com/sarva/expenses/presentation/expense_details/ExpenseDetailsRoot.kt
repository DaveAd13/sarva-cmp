package com.sarva.expenses.presentation.expense_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.domain.model.expense.Expense
import com.sarva.core.domain.model.expense.ExpenseCategory
import com.sarva.core.presentation.CategoryIcon
import com.sarva.core.presentation.dialogs.SimpleDialog
import com.sarva.core.presentation.getIcon
import com.sarva.core.presentation.getLabel
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.core.presentation.util.ResultStore
import com.sarva.core.presentation.util.formatToLongDisplay
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.breakdown
import com.sarva.features.expenses.generated.resources.cancel
import com.sarva.features.expenses.generated.resources.delete
import com.sarva.features.expenses.generated.resources.delete_expense
import com.sarva.features.expenses.generated.resources.delete_expense_description
import com.sarva.features.expenses.generated.resources.expense_details
import com.sarva.features.expenses.generated.resources.failed_to_load_expense
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ExpenseDetailsRoot(
    expenseId: Int,
    resultStore: ResultStore,
    onEditExpenseClicked: (Int) -> Unit,
    viewModel: ExpenseDetailsViewModel = koinViewModel { parametersOf(expenseId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onBack = LocalBackHandler.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ExpenseDetailsEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message.asString())
                }
            }

            ExpenseDetailsEvent.ExpenseLoadingFailed -> {
                resultStore.setResult("expense_loading_failed", true)
                onBack()
            }

            ExpenseDetailsEvent.ExpenseDeleted -> {
                resultStore.setResult("expense_deleted", true)
                onBack()
            }

            ExpenseDetailsEvent.OnEditClicked -> {
                onEditExpenseClicked(expenseId)
            }
        }
    }

    ExpenseDetailsScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailsScreen(
    state: ExpenseDetailsState,
    snackbarHostState: SnackbarHostState,
    onAction: (ExpenseDetailsAction) -> Unit,
    onBack: () -> Unit
) {
    val expense = state.expense
    val containerColor = SarvaTheme.colors.expenseContainer
    val contentColor = SarvaTheme.colors.expenseContent
    val cardContainerColor = SarvaTheme.colors.expenseCardContainer

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.expense_details),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
                    if (expense != null) {
                        IconButton(onClick = { onAction(ExpenseDetailsAction.OnEditClicked) }) {
                            Icon(Icons.Rounded.Edit, null)
                        }
                        IconButton(onClick = { onAction(ExpenseDetailsAction.OnDeleteClicked) }) {
                            Icon(Icons.Rounded.Delete, null)
                        }
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
        containerColor = containerColor
    ) { contentPadding ->
        if (expense == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Text(
                    text = stringResource(Res.string.failed_to_load_expense),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = cardContainerColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${expense.currency} ${expense.amount}",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = contentColor
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = expense.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = contentColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = contentColor.copy(alpha = 0.1f),
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val painter = when (val iconData = expense.category.getIcon()) {
                                    is CategoryIcon.Vector -> rememberVectorPainter(iconData.imageVector)
                                    is CategoryIcon.Custom -> painterResource(iconData.resource)
                                }
                                Icon(
                                    painter = painter,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = contentColor
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = expense.category.getLabel().asStringC(),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = contentColor
                                    ),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = expense.dateTime.formatToLongDisplay(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = contentColor.copy(alpha = 0.7f)
                        )

                        if (expense.location != null) {
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(color = contentColor.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp),
                                    tint = contentColor.copy(alpha = 0.7f)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    expense.location?.let { location ->
                                        Text(
                                            text = location.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = contentColor
                                        )
                                        location.city?.let { city ->
                                            Text(
                                                text = city,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = contentColor.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (expense.entries.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = contentColor.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = stringResource(Res.string.breakdown),
                                modifier = Modifier.align(Alignment.Start),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = contentColor.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Medium,
                                ),
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            expense.entries.forEach { entry ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        entry.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = contentColor
                                    )
                                    Text(
                                        text = "${expense.currency} ${entry.price}",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontFeatureSettings = "tnum",
                                        ),
                                        color = contentColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        SimpleDialog(
            title = stringResource(Res.string.delete_expense),
            description = stringResource(Res.string.delete_expense_description),
            icon = Icons.Rounded.DeleteForever,
            confirmText = stringResource(Res.string.delete),
            dismissText = stringResource(Res.string.cancel),
            containerColor = containerColor,
            contentColor = contentColor,
            confirmLabelColor = contentColor,
            dismissLabelColor = contentColor,
            onConfirm = { onAction(ExpenseDetailsAction.OnDeleteConfirmed) },
            onDismiss = { onAction(ExpenseDetailsAction.OnDeleteCancelled) }
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        ExpenseDetailsScreen(
            state = ExpenseDetailsState(
                expense = Expense(
                    title = "Trip to Dilijan",
                    amount = 370.0,
                    currency = "USD",
                    category = ExpenseCategory.TRAVEL,
                    dateTime = LocalDateTime.parse("2024-09-10T00:00:00"),
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
            onBack = {}
        )
    }
}