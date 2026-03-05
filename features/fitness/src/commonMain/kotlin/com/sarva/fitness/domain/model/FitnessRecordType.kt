package com.sarva.fitness.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Map
import com.sarva.core.presentation.generated.resources.Res
import com.sarva.core.presentation.generated.resources.ic_steps_24
import com.sarva.core.presentation.models.IconSource

enum class FitnessRecordType {
    STEPS, CALORIES, DISTANCE;

    val icon: IconSource
        get() = when (this) {
            STEPS -> IconSource.Resource(Res.drawable.ic_steps_24)
            CALORIES -> IconSource.Vector(Icons.Rounded.LocalFireDepartment)
            DISTANCE -> IconSource.Vector(Icons.Rounded.Map)
        }
}