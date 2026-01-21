package com.sarva.core.presentation.util

import androidx.compose.runtime.staticCompositionLocalOf

val LocalBackHandler = staticCompositionLocalOf<() -> Unit> {
    error("No BackHandler provided")
}