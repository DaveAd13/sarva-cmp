package com.sarva.app.features.home.presentation.components

import androidx.compose.runtime.Composable
import com.sarva.app.features.home.presentation.HomeEvent
import com.sarva.core.presentation.util.ObserveAsEvents
import kotlinx.coroutines.flow.Flow

@Composable
actual fun HealthPermissionEffect(
    events: Flow<HomeEvent>,
    onPermissionGranted: () -> Unit
) {
    //TODO Trigger iOS HealthKit permission request

    ObserveAsEvents(events) { event ->
        when (event) {
            HomeEvent.RequestHealthPermission -> {
                // TODO: Trigger iOS HealthKit permission request
            }
            else -> {}
        }
    }
}