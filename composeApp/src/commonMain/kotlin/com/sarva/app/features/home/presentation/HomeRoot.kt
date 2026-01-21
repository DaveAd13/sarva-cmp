package com.sarva.app.features.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.app.features.home.presentation.components.widgets.CalendarWidget
import com.sarva.app.features.home.presentation.components.widgets.ExpensesWidget
import com.sarva.app.features.home.presentation.components.widgets.FitnessWidget
import com.sarva.app.features.home.presentation.components.widgets.NotesWidget
import com.sarva.app.features.home.presentation.components.widgets.PlacesWidget
import com.sarva.app.features.home.presentation.components.widgets.TaskWidget
import com.sarva.app.features.home.presentation.components.HealthPermissionEffect
import com.sarva.designsystem.theme.SarvaTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (HomeNavigationAction) -> Unit,
    hazeState: HazeState,
    contentPadding: PaddingValues,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HealthPermissionEffect(
        events = viewModel.events,
        onPermissionGranted = viewModel::onPermissionGranted
    )

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            HomeEvent.NavigateToTODO -> {

            }

            else -> {}
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        hazeState = hazeState,
        contentPadding = contentPadding,
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigate: (HomeNavigationAction) -> Unit,
    hazeState: HazeState,
    contentPadding: PaddingValues,
) {
    val layoutDirection = LocalLayoutDirection.current
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = state.isLoading,
        onRefresh = { onAction(HomeAction.OnRefresh) },
        indicator = {
            PullToRefreshDefaults.Indicator(
                isRefreshing = state.isLoading,
                state = pullToRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = contentPadding.calculateTopPadding()),
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
            contentPadding = PaddingValues(
                start = 12.dp + contentPadding.calculateStartPadding(layoutDirection),
                end = 12.dp + contentPadding.calculateEndPadding(layoutDirection),
                top = 16.dp + contentPadding.calculateTopPadding(),
                bottom = 16.dp + contentPadding.calculateBottomPadding()
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                AnimatedContent(
                    targetState = state.hasHealthPermission,
                    label = "health_permission_swap"
                ) { hasPermission ->
                    if (hasPermission) {
                        FitnessWidget(
                            steps = state.steps,
                            goal = state.stepsGoal,
                            onWidgetClick = { onNavigate(HomeNavigationAction.OpenFitness) },
                            modifier = Modifier.aspectRatio(1f)
                        )
                    } else {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            onClick = { onAction(HomeAction.RequestHealthPermission) },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = SarvaTheme.colors.fitnessContainer),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.HealthAndSafety,
                                    contentDescription = null,
                                    tint = SarvaTheme.colors.fitnessContent,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Connect Health",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = SarvaTheme.colors.fitnessContent
                                )
                            }
                        }
                    }
                }
            }
            item {
                ExpensesWidget(
                    spentInfo = state.spentInfo,
                    onWidgetClick = { onNavigate(HomeNavigationAction.OpenExpenses) },
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }

            item {
                NotesWidget(
                    count = state.notesCount,
                    recentNote = state.recentNote,
                    onWidgetClick = { onNavigate(HomeNavigationAction.OpenNotes) },
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }

            item {
                TaskWidget(
                    tasks = state.tasks,
                    onTaskToggle = { id -> onAction(HomeAction.OnTaskToggle(id)) },
                    onWidgetClick = { onNavigate(HomeNavigationAction.OpenTasks) },
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }

            item {
                CalendarWidget(
                    event = state.event,
                    onWidgetClick = { /* navigate */ },
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }

            item {
                PlacesWidget(
                    onWidgetClick = { onNavigate(HomeNavigationAction.OpenPlaces) },
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Preview(name = "Light")
@Composable
private fun PreviewLight() {
    SarvaTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                state = HomeState(hasHealthPermission = true),
                onAction = {},
                onNavigate = {},
                hazeState = rememberHazeState(),
                contentPadding = PaddingValues(),
            )
        }
    }
}

@Preview(name = "Dark")
@Composable
private fun PreviewDark() {
    SarvaTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                state = HomeState(hasHealthPermission = true),
                onAction = {},
                onNavigate = {},
                hazeState = rememberHazeState(),
                contentPadding = PaddingValues(),
            )
        }
    }
}