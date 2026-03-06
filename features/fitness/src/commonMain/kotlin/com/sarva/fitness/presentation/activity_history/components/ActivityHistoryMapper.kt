package com.sarva.fitness.presentation.activity_history.components

import com.sarva.core.presentation.formatting.formatNumber
import com.sarva.core.presentation.util.UiText
import com.sarva.features.fitness.generated.resources.Res
import com.sarva.features.fitness.generated.resources.cal
import com.sarva.features.fitness.generated.resources.km
import com.sarva.features.fitness.generated.resources.steps
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.FitnessRecordType

object ActivityHistoryMapper {

    fun mapToOverallValue(activity: FitnessActivity, type: FitnessRecordType): String {
        val rawValue = when (type) {
            FitnessRecordType.STEPS -> activity.totalSteps().toDouble()
            FitnessRecordType.CALORIES -> activity.totalCalories()
            FitnessRecordType.DISTANCE -> activity.totalDistance()
        }
        return formatNumber(rawValue)
    }

    fun mapToOverallLabel(type: FitnessRecordType): UiText {
        return when (type) {
            FitnessRecordType.STEPS -> UiText.StringRes(Res.string.steps)
            FitnessRecordType.CALORIES -> UiText.StringRes(Res.string.cal)
            FitnessRecordType.DISTANCE -> UiText.StringRes(Res.string.km)
        }
    }
}