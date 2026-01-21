package com.sarva.expenses.presentation.expense_details

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExpenseDetailsRoot(
    viewModel: ExpenseDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    ExpenseDetailsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ExpenseDetailsScreen(
    state: ExpenseDetailsState,
    onAction: (ExpenseDetailsAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    SarvaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExpenseDetailsScreen(
                state = ExpenseDetailsState(),
                onAction = {}
            )
        }
    }
}