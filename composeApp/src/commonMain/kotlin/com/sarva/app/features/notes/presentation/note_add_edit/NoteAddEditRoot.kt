package com.sarva.app.features.notes.presentation.note_add_edit

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
fun NoteAddEditRoot(
    noteId: String? = null,
    viewModel: NoteAddEditViewModel = koinViewModel(),
    onSaveFinished: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    NoteAddEditScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NoteAddEditScreen(
    state: NoteAddEditState,
    onAction: (NoteAddEditAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    SarvaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NoteAddEditScreen(
                state = NoteAddEditState(),
                onAction = {}
            )
        }
    }
}