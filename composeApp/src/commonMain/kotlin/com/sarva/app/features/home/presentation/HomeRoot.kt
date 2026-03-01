package com.sarva.app.features.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.domain.settings.model.WidgetLayout
import com.sarva.app.features.home.presentation.components.HealthPermissionEffect
import com.sarva.app.features.home.presentation.components.widgets.CalendarWidget
import com.sarva.app.features.home.presentation.components.widgets.ExpensesWidget
import com.sarva.app.features.home.presentation.components.widgets.FitnessWidget
import com.sarva.app.features.home.presentation.components.widgets.NotesWidget
import com.sarva.app.features.home.presentation.components.widgets.PlacesWidget
import com.sarva.app.features.home.presentation.components.widgets.TaskWidget
import com.sarva.app.generated.resources.Res
import com.sarva.app.generated.resources.home
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (HomeNavigationAction) -> Unit,
    contentPadding: PaddingValues,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HealthPermissionEffect(
        events = viewModel.events,
        onPermissionGranted = viewModel::onPermissionGranted
    )

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        contentPadding = contentPadding,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigate: (HomeNavigationAction) -> Unit,
    contentPadding: PaddingValues,
) {
    val layoutDirection = LocalLayoutDirection.current
    val pullToRefreshState = rememberPullToRefreshState()
    val gridState = rememberLazyGridState()
    val hazeState = rememberHazeState()
    val isScrolled by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
        }
    }
    val hazeAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        label = "hazeFade"
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isLandscape = maxWidth > maxHeight
        val isTiled = state.widgetLayout == WidgetLayout.TILED

        val columns = when {
            !isTiled -> 1
            isLandscape -> 4
            else -> 2
        }

        val targetRatio = when {
            !isTiled && isLandscape -> 3.5f
            !isTiled -> 2f
            else -> 1f
        }

        val animatedRatio by animateFloatAsState(
            targetValue = targetRatio,
            animationSpec = tween(250, easing = FastOutSlowInEasing)
        )

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
                        .padding(top = 64.dp + contentPadding.calculateTopPadding()),
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                state = gridState,
                contentPadding = PaddingValues(
                    start = 12.dp + contentPadding.calculateStartPadding(layoutDirection),
                    end = 12.dp + contentPadding.calculateEndPadding(layoutDirection),
                    top = 64.dp + 16.dp + contentPadding.calculateTopPadding(),
                    bottom = 16.dp + contentPadding.calculateBottomPadding()
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(
                    key = "health",
                ) {
                    FitnessWidget(
                        state = state,
                        onWidgetClick = {
                            if (state.hasHealthPermission) {
                                onNavigate(HomeNavigationAction.OpenFitness)
                            } else {
                                onAction(HomeAction.RequestHealthPermission)
                            }
                        },
                        modifier = Modifier.widgetModifier(this, animatedRatio)
                    )
                }

                item(
                    key = "expenses",
                ) {
                    ExpensesWidget(
                        spentInfo = state.spentInfo,
                        onWidgetClick = { onNavigate(HomeNavigationAction.OpenExpenses) },
                        modifier = Modifier.widgetModifier(this, animatedRatio)
                    )
                }

                item(
                    key = "notes",
                ) {
                    NotesWidget(
                        count = state.notesCount,
                        recentNote = state.recentNote,
                        onWidgetClick = { onNavigate(HomeNavigationAction.OpenNotes) },
                        modifier = Modifier.widgetModifier(this, animatedRatio)
                    )
                }

                item(
                    key = "tasks",
                ) {
                    TaskWidget(
                        tasks = state.tasks,
                        onTaskToggle = { id -> onAction(HomeAction.OnTaskToggle(id)) },
                        onWidgetClick = { onNavigate(HomeNavigationAction.OpenTasks) },
                        modifier = Modifier.widgetModifier(this, animatedRatio)
                    )
                }

                item(
                    key = "calendar",
                ) {
                    CalendarWidget(
                        event = state.event,
                        onWidgetClick = { onNavigate(HomeNavigationAction.OpenCalendar) },
                        modifier = Modifier.widgetModifier(this, animatedRatio)
                    )
                }

                item(
                    key = "places",
                ) {
                    PlacesWidget(
                        onWidgetClick = { onNavigate(HomeNavigationAction.OpenPlaces) },
                        modifier = Modifier.widgetModifier(this, animatedRatio)
                    )
                }
            }
        }

        TopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.home),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .hazeEffect(hazeState) {
                    blurRadius = 15.dp * hazeAlpha
                    progressive = HazeProgressive.verticalGradient(
                        startIntensity = 1f * hazeAlpha,
                        endIntensity = 0f
                    )
                },
            actions = {
                IconButton(onClick = { onAction(HomeAction.OnGridTypeClicked) }) {
                    AnimatedContent(targetState = state.widgetLayout) { targetGridType ->
                        val icon =
                            if (targetGridType == WidgetLayout.STACKED) Icons.Filled.Window
                            else Icons.Filled.ViewStream
                        Icon(icon, null)

                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
        )
    }
}

fun Modifier.widgetModifier(
    itemScope: LazyGridItemScope,
    animatedRatio: Float,
    animationDuration: Int = 250
): Modifier = with(itemScope) {
    this@widgetModifier
        .animateItem(
            placementSpec = tween(animationDuration, easing = FastOutSlowInEasing),
            fadeInSpec = tween(animationDuration),
            fadeOutSpec = tween(animationDuration)
        )
        .aspectRatio(animatedRatio)
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                state = HomeState(hasHealthPermission = true),
                onAction = {},
                onNavigate = {},
                contentPadding = PaddingValues(),
            )
        }
    }
}