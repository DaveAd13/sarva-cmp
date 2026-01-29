package com.sarva.core.presentation.textfields

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ClearTextIcon(
    color: Color,
    onClear: () -> Unit,
) {
    IconButton(
        onClick = onClear
    ) {
        Icon(
            imageVector = Icons.Rounded.Cancel,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
    }
}