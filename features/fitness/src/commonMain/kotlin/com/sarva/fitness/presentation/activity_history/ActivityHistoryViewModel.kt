package com.sarva.fitness.presentation.activity_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.util.Result
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.FitnessRecordType
import com.sarva.fitness.domain.usecase.GetActivityHistoryUseCase
import com.sarva.fitness.domain.util.ActivityDateTimeCalculator.calculateNewDate
import com.sarva.fitness.domain.util.ActivityDateTimeCalculator.canGoForward
import com.sarva.fitness.presentation.activity_history.components.ActivityHistoryMapper.mapToOverallLabel
import com.sarva.fitness.presentation.activity_history.components.ActivityHistoryMapper.mapToOverallValue
import com.sarva.fitness.presentation.util.formatPeriodLabel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityHistoryViewModel(
    private val recordType: FitnessRecordType,
    private val getActivityHistoryUseCase: GetActivityHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ActivityHistoryState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(recordType = recordType) }

        state
            .map { it.period to it.anchorDate }
            .distinctUntilChanged()
            .mapLatest { (period, date) ->
                getActivityHistoryUseCase(period, date)
            }
            .onEach { result ->
                _state.update { currentState ->
                    when (result) {
                        is Result.Success -> currentState.copy(
                            fitnessActivity = result.data,
                            isLoading = false
                        ).run(::calculateDerivedState)

                        is Result.Failure -> currentState.copy(
                            isLoading = false
                        ).run(::calculateDerivedState)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateState(
        period: ActivityPeriod,
        date: LocalDate,
    ) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val validatedDate = if (date > today) today else date

        _state.update {
            it.copy(
                anchorDate = validatedDate,
                period = period,
                isLoading = true,
            ).run(::calculateDerivedState)
        }
    }

    fun onAction(action: ActivityHistoryAction) {
        val currentState = _state.value

        when (action) {
            is ActivityHistoryAction.ChangePeriod -> {
                updateState(
                    period = action.period,
                    date = currentState.anchorDate,
                )
            }

            is ActivityHistoryAction.ChangeRecordType -> {
                _state.update {
                    it.copy(
                        recordType = action.recordType,
                    ).run(::calculateDerivedState)
                }
            }

            ActivityHistoryAction.GoBack -> {
                val newDate = calculateNewDate(currentState.period, currentState.anchorDate, -1)
                updateState(
                    period = currentState.period,
                    date = newDate,
                )
            }

            ActivityHistoryAction.GoForward -> {
                val newDate = calculateNewDate(currentState.period, currentState.anchorDate, 1)
                updateState(
                    period = currentState.period,
                    date = newDate,
                )
            }
        }
    }

    private fun calculateDerivedState(state: ActivityHistoryState): ActivityHistoryState {
        return state.copy(
            periodDateLabel = formatPeriodLabel(state.period, state.anchorDate),
            canGoForward = canGoForward(state.period, state.anchorDate),
            periodOverall = mapToOverallValue(state.fitnessActivity, state.recordType),
            periodOverallLabel = mapToOverallLabel(state.recordType)
        )
    }
}
