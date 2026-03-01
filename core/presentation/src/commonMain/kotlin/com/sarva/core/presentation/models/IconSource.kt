package com.sarva.core.presentation.models

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

sealed class IconSource {
    data class Vector(val imageVector: ImageVector) : IconSource()
    data class Resource(val resource: DrawableResource) : IconSource()
}