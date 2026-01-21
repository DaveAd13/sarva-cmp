package com.sarva.expenses.presentation.expense_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.core.presentation.util.ResultStore
import com.sarva.designsystem.theme.SarvaTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ExpenseDetailsRoot(
    expenseId: Int,
    resultStore: ResultStore,
    viewModel: ExpenseDetailsViewModel = koinViewModel { parametersOf(expenseId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onBack = LocalBackHandler.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ExpenseDetailsEvent.ShowSnackbar -> {

            }

            ExpenseDetailsEvent.ExpenseLoadingFailed -> {
                resultStore.setResult("expense_loading_failed", true)
                onBack()
            }
        }
    }

    ExpenseDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack
    )
}

@Composable
fun ExpenseDetailsScreen(
    state: ExpenseDetailsState,
    onAction: (ExpenseDetailsAction) -> Unit,
    onBack: () -> Unit
) {

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
        ExpenseDetailsScreen(
            state = ExpenseDetailsState(),
            onAction = {},
            onBack = {}
        )
    }
}