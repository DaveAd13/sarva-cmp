package com.sarva.app.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarva.core.domain.settings.model.WidgetLayout
import com.sarva.core.domain.settings.repository.UserSettingsRepository
import com.sarva.core.domain.util.Result
import com.sarva.core.presentation.formatting.formatNumber
import com.sarva.fitness.domain.usecase.CheckHealthPermissionsUseCase
import com.sarva.fitness.domain.usecase.GetDailyRecordsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getDailyRecordsUseCase: GetDailyRecordsUseCase,
    private val hasHealthPermission: CheckHealthPermissionsUseCase,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val settings = userSettingsRepository.getUserSettings()
            _state.update {
                it.copy(
                    widgetLayout = settings.homeLayout,
                )
            }
        }

        viewModelScope.launch {
            if (hasHealthPermission()) {
                _state.update {
                    it.copy(
                        hasHealthPermission = true
                    )
                }
                loadDailyRecords()
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
            loadDailyRecords()
        }
    }

    fun loadDailyRecords() {
        viewModelScope.launch {
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

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> {
                viewModelScope.launch {
                    loadDailyRecords()
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

            HomeAction.OnGridTypeClicked -> {
                val newLayout = when (_state.value.widgetLayout) {
                    WidgetLayout.TILED -> WidgetLayout.STACKED
                    WidgetLayout.STACKED -> WidgetLayout.TILED
                }

                _state.update { it.copy(widgetLayout = newLayout) }

                viewModelScope.launch {
                    userSettingsRepository.updateHomeLayout(newLayout)
                }
            }
        }
    }
}