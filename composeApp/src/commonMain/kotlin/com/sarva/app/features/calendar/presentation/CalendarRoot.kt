package com.sarva.app.features.calendar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme

@Composable
fun CalendarRoot(
    viewModel: CalendarViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    CalendarScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CalendarScreen(
    state: CalendarState,
    onAction: (CalendarAction) -> Unit,
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
        CalendarScreen(
            state = CalendarState(),
            onAction = {}
        )
    }
}