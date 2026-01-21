package com.sarva.core.presentation.snackbars

import androidx.compose.material3.SnackbarDuration
import com.sarva.core.presentation.util.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarController {
    private val _events = Channel<SnackbarEvent>(capacity = Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}

data class SnackbarEvent(
    val message: UiText,
    val action: SnackbarAction? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

data class SnackbarAction(
    val label: UiText,
    val action: suspend () -> Unit
)