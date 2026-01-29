package com.sarva.app.features.profile.presentation

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
fun ProfileRoot(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {

}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}