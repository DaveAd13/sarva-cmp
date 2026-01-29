package com.sarva.app.features.tasks.presentation.task_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TaskListRoot(
    viewModel: TaskListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    TaskListScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun TaskListScreen(
    state: TaskListState,
    onAction: (TaskListAction) -> Unit,
) {
    val containerColor = SarvaTheme.colors.taskContainer
    val contentColor = SarvaTheme.colors.taskContent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detailed Tasks Here",
                color = contentColor
            )
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TaskListScreen(
                state = TaskListState(),
                onAction = {}
            )
        }
    }
}