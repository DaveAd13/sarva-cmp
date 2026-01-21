package com.sarva.app.features.notes.presentation.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteListRoot(
    viewModel: NoteListViewModel = koinViewModel(),
    onNoteClick: (String) -> Unit,
    onAddNoteClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    NoteListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NoteListScreen(
    state: NoteListState,
    onAction: (NoteListAction) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
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
        NoteListScreen(
            state = NoteListState(),
            onAction = {}
        )
    }
}