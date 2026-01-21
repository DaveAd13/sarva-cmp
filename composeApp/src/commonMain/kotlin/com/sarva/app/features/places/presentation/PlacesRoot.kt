package com.sarva.app.features.places.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlacesRoot(
    viewModel: PlacesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    PlacesScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun PlacesScreen(
    state: PlacesState,
    onAction: (PlacesAction) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
//        MapComponent()
    }
}

@Preview
@Composable
private fun Preview() {
    SarvaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PlacesScreen(
                state = PlacesState(),
                onAction = {},
            )
        }
    }
}