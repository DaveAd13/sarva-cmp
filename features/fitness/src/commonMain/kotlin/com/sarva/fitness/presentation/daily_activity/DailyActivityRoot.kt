package com.sarva.fitness.presentation.daily_activity

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarva.core.presentation.formatting.formatNumber
import com.sarva.core.presentation.formatting.rememberDurationSymbols
import com.sarva.core.presentation.util.LocalBackHandler
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.features.fitness.generated.resources.Res
import com.sarva.features.fitness.generated.resources.cal
import com.sarva.features.fitness.generated.resources.daily_activity
import com.sarva.features.fitness.generated.resources.km
import com.sarva.features.fitness.generated.resources.no_exercises
import com.sarva.features.fitness.generated.resources.recent_exercises
import com.sarva.features.fitness.generated.resources.steps
import com.sarva.fitness.domain.model.FitnessExercise
import com.sarva.fitness.domain.model.FitnessRecordType
import com.sarva.fitness.presentation.components.FitnessExerciseCard
import com.sarva.fitness.presentation.daily_activity.components.FitnessStatCard
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DailyActivityRoot(
    viewModel: DailyActivityViewModel = koinViewModel(),
    onOpenFitnessDetails: (FitnessRecordType) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onBack = LocalBackHandler.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> TODO("Handle events")
        }
    }

    DailyActivityScreen(
        state = state,
        onAction = viewModel::onAction,
        onOpenFitnessDetails = onOpenFitnessDetails,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyActivityScreen(
    state: DailyActivityState,
    onAction: (DailyActivityAction) -> Unit,
    onOpenFitnessDetails: (FitnessRecordType) -> Unit,
    onBack: () -> Unit
) {
    val containerColor = SarvaTheme.colors.fitnessContainer
    val contentColor = SarvaTheme.colors.fitnessContent
    val successColor = SarvaTheme.colors.fitnessSuccess

    val durationSymbols = rememberDurationSymbols()
    val isGoalReached = remember(state.steps, state.goal) {
        state.steps >= state.goal
    }

    val animatedColor by animateColorAsState(
        targetValue = if (isGoalReached) successColor else contentColor,
        animationSpec = tween(500),
        label = "ColorAnimation"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = tween(1000, easing = LinearOutSlowInEasing),
        label = "ProgressAnimation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.daily_activity),
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
                    containerColor = containerColor,
                    titleContentColor = contentColor,
                    navigationIconContentColor = contentColor,
                )
            )
        },
    ) { contentPadding ->
        val layoutDirection = LocalLayoutDirection.current
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .background(containerColor),
            contentPadding = PaddingValues(
                start = contentPadding.calculateStartPadding(layoutDirection) + 24.dp,
                top = contentPadding.calculateTopPadding(),
                end = contentPadding.calculateEndPadding(layoutDirection) + 24.dp,
                bottom = contentPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = { onOpenFitnessDetails(FitnessRecordType.STEPS) },
                                role = Role.Button
                            )
                    ) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxSize(),
                            color = animatedColor,
                            trackColor = animatedColor.copy(alpha = 0.2f),
                            strokeWidth = 12.dp,
                            strokeCap = StrokeCap.Round
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val iconVector = remember(isGoalReached) {
                                if (isGoalReached) Icons.Rounded.EmojiEvents
                                else Icons.AutoMirrored.Rounded.DirectionsRun
                            }

                            Crossfade(
                                targetState = iconVector,
                                label = "IconFade"
                            ) { targetIcon ->
                                Icon(
                                    imageVector = targetIcon,
                                    contentDescription = null,
                                    tint = animatedColor,
                                    modifier = Modifier.size(48.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = formatNumber(state.steps),
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                            )
                            Text(
                                text = "${formatNumber(state.goal)} ${stringResource(Res.string.steps).lowercase()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = contentColor.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FitnessStatCard(
                        icon = Icons.Rounded.LocalFireDepartment,
                        value = state.calories,
                        label = stringResource(Res.string.cal),
                        color = contentColor,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onOpenFitnessDetails(FitnessRecordType.CALORIES) }
                    )

                    FitnessStatCard(
                        icon = Icons.Rounded.Map,
                        value = state.distance,
                        label = stringResource(Res.string.km),
                        color = contentColor,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onOpenFitnessDetails(FitnessRecordType.DISTANCE) }
                    )
                }
            }
            item {
                Text(
                    text = stringResource(if (state.exercises.isEmpty()) Res.string.no_exercises else Res.string.recent_exercises),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = contentColor
                    ),
                    modifier = Modifier.padding(start = 4.dp, top = 16.dp)
                )
            }

            items(
                items = state.exercises,
                key = { it.id }
            ) { exercise ->
                FitnessExerciseCard(
                    exercise = exercise,
                    symbols = durationSymbols,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                )
            }
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        DailyActivityScreen(
            state = DailyActivityState(
                progress = 0.7f, exercises = persistentListOf(
                    FitnessExercise(
                        id = "1",
                        type = "Walking",
                        icon = Icons.AutoMirrored.Rounded.DirectionsWalk,
                        startTime = LocalDateTime.parse("2023-01-02T23:40"),
                        endTime = LocalDateTime.parse("2023-01-02T23:40"),
                        durationSeconds = 1576
                    ),
                    FitnessExercise(
                        id = "2",
                        type = "Running",
                        icon = Icons.AutoMirrored.Rounded.DirectionsRun,
                        startTime = LocalDateTime.parse("2023-01-02T23:40"),
                        endTime = LocalDateTime.parse("2023-01-02T23:40"),
                        durationSeconds = 30
                    )
                )
            ),
            onAction = {},
            onOpenFitnessDetails = {},
            onBack = {}
        )
    }
}
