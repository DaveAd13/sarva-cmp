package com.sarva.core.presentation.snackbars

import androidx.compose.material3.SnackbarDuration
import com.sarva.core.presentation.util.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarManager {

    fun showSnackbar(
        scope: CoroutineScope,
        message: UiText,
        actionLabel: UiText? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onAction: (suspend () -> Unit)? = null
    ) {
        scope.launch {
            SnackbarController.sendEvent(
                SnackbarEvent(
                    message = message,
                    duration = duration,
                    action = actionLabel?.let { label ->
                        onAction?.let { logic -> SnackbarAction(label, logic) }
                    }
                )
            )
        }
    }
}

