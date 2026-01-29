package com.sarva.app.features.notes.presentation.note_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteDetailsRoot(
    noteId: String,
    viewModel: NoteDetailsViewModel = koinViewModel(),
    onEditClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    NoteDetailsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NoteDetailsScreen(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
) {

}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        NoteDetailsScreen(
            state = NoteDetailsState(),
            onAction = {}
        )
    }
}