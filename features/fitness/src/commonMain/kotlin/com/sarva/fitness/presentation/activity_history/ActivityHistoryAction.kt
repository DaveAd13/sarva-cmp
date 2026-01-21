package com.sarva.fitness.presentation.activity_history

import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessRecordType

sealed interface ActivityHistoryAction {
    data class ChangePeriod(val period: ActivityPeriod) : ActivityHistoryAction
    data class ChangeRecordType(val recordType: FitnessRecordType) : ActivityHistoryAction
    data object GoBack : ActivityHistoryAction
    data object GoForward : ActivityHistoryAction
}