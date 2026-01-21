package com.sarva.app.features.home.presentation.components

import androidx.compose.runtime.Composable
import com.sarva.app.features.home.presentation.HomeEvent
import kotlinx.coroutines.flow.Flow

@Composable
expect fun HealthPermissionEffect(
    events: Flow<HomeEvent>,
    onPermissionGranted: () -> Unit
)