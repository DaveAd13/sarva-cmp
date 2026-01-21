package com.sarva.fitness.presentation.daily_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.fitness.domain.usecase.GetExercisesUseCase
import com.sarva.fitness.domain.usecase.GetDailyRecordsUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System.now
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.util.formatNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class DailyActivityViewModel(
    private val getDailyRecordsUseCase: GetDailyRecordsUseCase,
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    companion object {
        const val STEP_GOAL = 10000
    }

    private val _state = MutableStateFlow(DailyActivityState())
    val state = _state
        .onStart {
            loadRecords()
            loadExercises()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DailyActivityState()
        )

    private val eventChannel = Channel<DailyActivityEvent>()
    val events = eventChannel.receiveAsFlow()

    fun loadRecords() {
        viewModelScope.launch(context = Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            when (val result = getDailyRecordsUseCase()) {
                is Result.Success -> {
                    result.data.run {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                steps = steps.toInt(),
                                distance = formatNumber(distance, 2),
                                calories = formatNumber(calories.toInt()),
                                progress = (steps.toFloat() / STEP_GOAL)
                            )
                        }
                    }
                }

                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch(context = Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val endDate = now().toLocalDateTime(TimeZone.currentSystemDefault())
            val startDate = endDate.date.atTime(0, 0)

            if (startDate >= endDate) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        exercises = persistentListOf()
                    )
                }
                return@launch
            }

            when (val result = getExercisesUseCase(startDate, endDate)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            exercises = result.data.toPersistentList()
                        )
                    }
                }

                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: DailyActivityAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}