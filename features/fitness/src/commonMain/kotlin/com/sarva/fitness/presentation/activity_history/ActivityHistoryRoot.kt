package com.sarva.fitness.presentation.activity_history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.formatting.rememberDurationSymbols
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.features.fitness.generated.resources.Res
import com.sarva.features.fitness.generated.resources.calories
import com.sarva.features.fitness.generated.resources.distance
import com.sarva.features.fitness.generated.resources.my_activity
import com.sarva.features.fitness.generated.resources.no_exercises
import com.sarva.features.fitness.generated.resources.recent_exercises
import com.sarva.features.fitness.generated.resources.steps
import com.sarva.fitness.domain.model.FitnessRecordType
import com.sarva.fitness.presentation.activity_history.components.PERIOD_TABS
import com.sarva.fitness.presentation.activity_history.components.history_chart.AnimatedFitnessChartContainer
import com.sarva.fitness.presentation.activity_history.components.history_chart.rememberChartDataLatched
import com.sarva.fitness.presentation.components.FitnessExerciseCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActivityHistoryRoot(
    recordType: FitnessRecordType,
    viewModel: ActivityHistoryViewModel = koinViewModel { parametersOf(recordType) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onBack = LocalBackHandler.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    ActivityHistoryScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityHistoryScreen(
    state: ActivityHistoryState,
    onAction: (ActivityHistoryAction) -> Unit,
    onBack: () -> Unit
) {
    val accentColor = SarvaTheme.colors.fitness
    val periodTabSelection = remember(state.period) {
        PERIOD_TABS.indexOfFirst { it.activityPeriod == state.period }
    }
    val layoutDirection = LocalLayoutDirection.current
    val chartData = rememberChartDataLatched(state)
    val durationSymbols = rememberDurationSymbols()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.my_activity),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    top = contentPadding.calculateTopPadding(),
                    end = contentPadding.calculateEndPadding(layoutDirection),
                )
        ) {
            PrimaryTabRow(
                selectedTabIndex = periodTabSelection,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = accentColor,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            periodTabSelection,
                            matchContentSize = true
                        ),
                        width = Dp.Unspecified,
                        color = accentColor,
                    )
                }
            ) {
                PERIOD_TABS.forEachIndexed { index, tab ->
                    Tab(
                        selected = periodTabSelection == index,
                        onClick = {
                            onAction(ActivityHistoryAction.ChangePeriod(tab.activityPeriod))
                        },
                        text = {
                            Text(
                                text = tab.label.asStringC(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        },
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    bottom = contentPadding.calculateBottomPadding() + 24.dp
                )
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onAction(ActivityHistoryAction.GoBack) },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.periodDateLabel,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )

                            Text(
                                text = "${state.periodOverall} ${
                                    state.periodOverallLabel.asStringC().lowercase()
                                }",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            )
                        }

                        IconButton(
                            onClick = { onAction(ActivityHistoryAction.GoForward) },
                            enabled = state.canGoForward,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.3f
                                )
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                }

                item {
                    AnimatedFitnessChartContainer(
                        data = chartData,
                        modifier = Modifier
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FitnessRecordType.entries.forEach { type ->
                            val isSelected = state.recordType == type
                            OutlinedButton(
                                modifier = Modifier
                                    .height(32.dp),
                                onClick = {
                                    onAction(ActivityHistoryAction.ChangeRecordType(type))
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) accentColor.copy(alpha = 0.15f) else Color.Transparent,
                                    contentColor = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = when (type) {
                                        FitnessRecordType.STEPS -> stringResource(Res.string.steps)
                                        FitnessRecordType.CALORIES -> stringResource(Res.string.calories)
                                        FitnessRecordType.DISTANCE -> stringResource(Res.string.distance)
                                    },
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(if (state.fitnessActivity.exercises.isEmpty()) Res.string.no_exercises else Res.string.recent_exercises),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp).padding(top = 12.dp)
                    )
                }

                items(
                    items = state.fitnessActivity.exercises,
                    key = { it.id }
                ) { exercise ->
                    FitnessExerciseCard(
                        exercise = exercise,
                        symbols = durationSymbols,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .animateItem()
                    )
                }
            }
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        ActivityHistoryScreen(
            state = ActivityHistoryState(
                periodDateLabel = "12-19 December",
            ),
            onAction = {},
            onBack = {},
        )
    }
}
