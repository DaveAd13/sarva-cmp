package com.sarva.app.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.fitness.domain.usecase.CheckHealthPermissionsUseCase
import com.sarva.fitness.domain.usecase.GetDailyRecordsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.sarva.core.domain.util.Result

class HomeViewModel(
    private val getDailyRecordsUseCase: GetDailyRecordsUseCase,
    private val hasHealthPermission: CheckHealthPermissionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            if (hasHealthPermission()) {
                _state.update {
                    it.copy(
                        hasHealthPermission = true
                    )
                }
                loadSteps()
            }
        }
    }

    fun onPermissionGranted() {
        _state.update {
            it.copy(
                hasHealthPermission = true
            )
        }
        viewModelScope.launch {
            loadSteps()
        }
    }

    fun loadSteps() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            when (val result = getDailyRecordsUseCase()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            steps = result.data.steps.toInt()
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

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> {
                viewModelScope.launch {
                    loadSteps()
                }
            }

            is HomeAction.OnTaskToggle -> {
                _state.update {
                    it.copy(
                        tasks = it.tasks.map { task ->
                            if (task.id == action.id) {
                                task.copy(isCompleted = !task.isCompleted)
                            } else {
                                task
                            }
                        }
                    )
                }
            }

            HomeAction.RequestHealthPermission -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.RequestHealthPermission)
                }
            }
        }
    }
}