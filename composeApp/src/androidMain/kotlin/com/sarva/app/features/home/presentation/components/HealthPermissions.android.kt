package com.sarva.app.features.home.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import com.sarva.app.features.home.presentation.HomeEvent
import com.sarva.core.presentation.util.ObserveAsEvents
import kotlinx.coroutines.flow.Flow

@Composable
actual fun HealthPermissionEffect(
    events: Flow<HomeEvent>,
    onPermissionGranted: () -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted: Set<String> ->
        if (granted.contains(
                HealthPermission.getReadPermission(StepsRecord::class)
            ) && granted.contains(
                HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
            ) && granted.contains(
                HealthPermission.getReadPermission(DistanceRecord::class)
            ) && granted.contains(
                HealthPermission.getReadPermission(ExerciseSessionRecord::class)
            )
        ) {
            onPermissionGranted()
        }
    }

    ObserveAsEvents(events) { event ->
        when (event) {
            HomeEvent.RequestHealthPermission -> {
                permissionLauncher.launch(
                    setOf(
                        HealthPermission.getReadPermission(StepsRecord::class),
                        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
                        HealthPermission.getReadPermission(DistanceRecord::class),
                        HealthPermission.getReadPermission(ExerciseSessionRecord::class)
                    )
                )
            }

            else -> {}
        }
    }
}