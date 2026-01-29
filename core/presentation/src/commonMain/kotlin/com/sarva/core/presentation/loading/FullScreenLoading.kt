package com.sarva.core.presentation.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex

@Composable
fun FullScreenLoading(
    containerColor: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor)
            .zIndex(1f)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = contentColor)
    }
}