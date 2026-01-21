package com.sarva.fitness.presentation.activity_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.util.UiText
import com.sarva.core.presentation.util.formatNumber
import com.sarva.features.fitness.generated.resources.Res
import com.sarva.features.fitness.generated.resources.cal
import com.sarva.features.fitness.generated.resources.km
import com.sarva.features.fitness.generated.resources.steps
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.ChartTransition
import com.sarva.fitness.domain.model.FitnessRecordType
import com.sarva.fitness.domain.usecase.GetActivityHistoryUseCase
import com.sarva.fitness.presentation.util.formatPeriodLabel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

//@OptIn(ExperimentalCoroutinesApi::class)
//class ActivityHistoryViewModel(
//    private val recordType: FitnessRecordType,
//    private val getActivityHistoryUseCase: GetActivityHistoryUseCase
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(ActivityHistoryState())
//    val state = _state.asStateFlow()
//
//    private val eventChannel = Channel<ActivityHistoryEvent>()
//    val events = eventChannel.receiveAsFlow()
//
//    init {
//        _state.update {
//            it.copy(
//                recordType = recordType,
//                periodDateLabel = formatPeriodLabel(it.period, it.anchorDate),
//            )
//        }
//
//        state
//            .map { it.period to it.anchorDate }
//            .distinctUntilChanged()
//            .flatMapLatest { (period, date) ->
//                flow {
//                    val result = getActivityHistoryUseCase(
//                        period = period,
//                        anchorDate = date
//                    )
//
//                    emit(result)
//                }
//            }.onEach { result ->
//                when (result) {
//                    is Result.Success -> {
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                fitnessActivity = result.data
//                            ).run(::calculateDerivedState)
//                        }
//                    }
//
//                    is Result.Failure -> {
//                        _state.update {
//                            it.copy(
//                                isLoading = false
//                            )
//                        }
//                    }
//                }
//
//            }.launchIn(viewModelScope)
//
//        _state.update { it.run(::calculateDerivedState) }
//    }
//
////    fun loadData(
////        period: ActivityPeriod,
////        date: LocalDate,
////        transition: ChartTransition = ChartTransition.DEFAULT
////    ) {
////        viewModelScope.launch {
////            when (val result = getActivityHistoryUseCase(period, date)) {
////                is Result.Success -> {
////                    _state.update {
////                        it.copy(
////                            isLoading = false,
////                            fitnessActivity = result.data
////                        )
////                    }
////                }
////
////                is Result.Failure -> {
////                    _state.update {
////                        it.copy(
////                            isLoading = false
////                        )
////                    }
////                }
////            }
////        }
////    }
//
//    private fun calculateDerivedState(state: ActivityHistoryState): ActivityHistoryState {
//        val overall = when (state.recordType) {
//            FitnessRecordType.STEPS -> formatNumber(state.fitnessActivity.records.sumOf { it.steps }
//                .toInt())
//
//            FitnessRecordType.CALORIES -> formatNumber(state.fitnessActivity.records.sumOf { it.calories }
//                .toInt())
//
//            FitnessRecordType.DISTANCE -> formatNumber(state.fitnessActivity.records.sumOf { it.distance })
//        }
//
//        val overallLabel = when (state.recordType) {
//            FitnessRecordType.STEPS -> StringRes(Res.string.steps)
//            FitnessRecordType.CALORIES -> StringRes(Res.string.cal)
//            FitnessRecordType.DISTANCE -> StringRes(Res.string.km)
//        }
//
//        return state.copy(
//            periodDateLabel = formatPeriodLabel(state.period, state.anchorDate),
//            canGoForward = canGoForward(state.period, state.anchorDate),
//            periodOverall = overall,
//            periodOverallLabel = overallLabel
//        )
//    }
//
//    private fun changeDate(steps: Int) {
//        _state.update { currentState ->
//            val newDate = when (currentState.period) {
//                ActivityPeriod.DAY -> currentState.anchorDate.plus(steps, DateTimeUnit.DAY)
//                ActivityPeriod.WEEK -> currentState.anchorDate.plus(steps, DateTimeUnit.WEEK)
//                ActivityPeriod.MONTH -> currentState.anchorDate.plus(steps, DateTimeUnit.MONTH)
//                ActivityPeriod.YEAR -> currentState.anchorDate.plus(steps, DateTimeUnit.YEAR)
//            }
//            currentState.copy(anchorDate = newDate).run(::calculateDerivedState)
//        }
//    }
//
//    @OptIn(ExperimentalTime::class)
//    private fun canGoForward(period: ActivityPeriod, anchorDate: LocalDate): Boolean {
//        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
//        return when (period) {
//            ActivityPeriod.DAY -> anchorDate < today
//            ActivityPeriod.WEEK -> {
//                val anchorMonday =
//                    anchorDate.minus(anchorDate.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
//                val todayMonday = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
//                anchorMonday < todayMonday
//            }
//
//            ActivityPeriod.MONTH -> {
//                anchorDate.year < today.year || (anchorDate.year == today.year && anchorDate.month < today.month)
//            }
//
//            ActivityPeriod.YEAR -> anchorDate.year < today.year
//        }
//    }
//
//    fun onAction(action: ActivityHistoryAction) {
//        when (action) {
//            is ActivityHistoryAction.ChangePeriod -> {
//                _state.update {
//                    it.copy(
//                        period = action.period,
//                        transition = ChartTransition.DEFAULT,
//                    ).run(::calculateDerivedState)
//                }
//            }
//
//            is ActivityHistoryAction.ChangeRecordType -> {
//                _state.update {
//                    it.copy(
//                        recordType = action.recordType,
//                        transition = ChartTransition.DEFAULT,
//                    ).run(::calculateDerivedState)
//                }
//            }
//
//            ActivityHistoryAction.GoBack -> {
//                changeDate(steps = -1)
//                _state.update {
//                    it.copy(
//                        transition = ChartTransition.BACKWARD,
//                    )
//                }
//            }
//
//            ActivityHistoryAction.GoForward -> {
//                changeDate(steps = 1)
//                _state.update {
//                    it.copy(
//                        transition = ChartTransition.FORWARD,
//                    )
//                }
//            }
//        }
//    }
//}

class ActivityHistoryViewModel(
    private val recordType: FitnessRecordType,
    private val getActivityHistoryUseCase: GetActivityHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ActivityHistoryState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ActivityHistoryEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        _state.update { it.copy(recordType = recordType) }

        loadData(
            period = _state.value.period,
            date = _state.value.anchorDate
        )
    }

    private fun loadData(
        period: ActivityPeriod,
        date: LocalDate,
        transition: ChartTransition = ChartTransition.DEFAULT
    ) {
        _state.update {
            it.copy(
                anchorDate = date,
                period = period,
                isLoading = true,
                transition = transition
            ).run(::calculateDerivedState)
        }

        viewModelScope.launch {
            when (val result = getActivityHistoryUseCase(period, date)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            fitnessActivity = result.data,
                            isLoading = false
                        ).run(::calculateDerivedState)
                    }
                }

                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        ).run(::calculateDerivedState)
                    }
                }
            }
        }
    }

    fun onAction(action: ActivityHistoryAction) {
        val currentState = _state.value

        when (action) {
            is ActivityHistoryAction.ChangePeriod -> {
                loadData(
                    period = action.period,
                    date = currentState.anchorDate,
                    transition = ChartTransition.DEFAULT
                )
            }

            is ActivityHistoryAction.ChangeRecordType -> {
                _state.update {
                    it.copy(
                        recordType = action.recordType,
                        transition = ChartTransition.DEFAULT,
                    ).run(::calculateDerivedState)
                }
            }

            ActivityHistoryAction.GoBack -> {
                val newDate = calculateNewDate(currentState.period, currentState.anchorDate, -1)
                loadData(
                    period = currentState.period,
                    date = newDate,
                    transition = ChartTransition.BACKWARD
                )
            }

            ActivityHistoryAction.GoForward -> {
                val newDate = calculateNewDate(currentState.period, currentState.anchorDate, 1)
                loadData(
                    period = currentState.period,
                    date = newDate,
                    transition = ChartTransition.FORWARD
                )
            }
        }
    }

    private fun calculateNewDate(
        period: ActivityPeriod,
        current: LocalDate,
        steps: Int
    ): LocalDate {
        return when (period) {
            ActivityPeriod.DAY -> current.plus(steps, DateTimeUnit.DAY)
            ActivityPeriod.WEEK -> current.plus(steps, DateTimeUnit.WEEK)
            ActivityPeriod.MONTH -> current.plus(steps, DateTimeUnit.MONTH)
            ActivityPeriod.YEAR -> current.plus(steps, DateTimeUnit.YEAR)
        }
    }

    private fun calculateDerivedState(state: ActivityHistoryState): ActivityHistoryState {
        val overall = when (state.recordType) {
            FitnessRecordType.STEPS -> formatNumber(state.fitnessActivity.records.sumOf { it.steps }
                .toInt())

            FitnessRecordType.CALORIES -> formatNumber(state.fitnessActivity.records.sumOf { it.calories }
                .toInt())

            FitnessRecordType.DISTANCE -> formatNumber(state.fitnessActivity.records.sumOf { it.distance })
        }

        val overallLabel = when (state.recordType) {
            FitnessRecordType.STEPS -> UiText.StringRes(Res.string.steps)
            FitnessRecordType.CALORIES -> UiText.StringRes(Res.string.cal)
            FitnessRecordType.DISTANCE -> UiText.StringRes(Res.string.km)
        }

        return state.copy(
            periodDateLabel = formatPeriodLabel(state.period, state.anchorDate),
            canGoForward = canGoForward(state.period, state.anchorDate),
            periodOverall = overall,
            periodOverallLabel = overallLabel
        )
    }

    @OptIn(ExperimentalTime::class)
    private fun canGoForward(period: ActivityPeriod, anchorDate: LocalDate): Boolean {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return when (period) {
            ActivityPeriod.DAY -> anchorDate < today
            ActivityPeriod.WEEK -> {
                val anchorMonday =
                    anchorDate.minus(anchorDate.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
                val todayMonday = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
                anchorMonday < todayMonday
            }

            ActivityPeriod.MONTH -> {
                anchorDate.year < today.year || (anchorDate.year == today.year && anchorDate.month < today.month)
            }

            ActivityPeriod.YEAR -> anchorDate.year < today.year
        }
    }
}
