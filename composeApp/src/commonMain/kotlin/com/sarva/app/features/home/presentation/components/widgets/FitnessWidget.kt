package com.sarva.app.features.home.presentation.components.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.core.presentation.util.formatNumber
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.app.features.home.presentation.HomeState

@Composable
fun FitnessWidget(
    steps: Int,
    goal: Int,
    onWidgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val containerColor = SarvaTheme.colors.fitnessContainer
    val contentColor = SarvaTheme.colors.fitnessContent

    val isGoalReached = steps >= goal
    val progressRaw = (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onWidgetClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FitnessGauge(
                progress = progressRaw,
                isGoalReached = isGoalReached,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = formatNumber(steps),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = contentColor
            )

            Text(
                text = "${formatNumber(goal)} steps",
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun FitnessGauge(
    progress: Float,
    isGoalReached: Boolean
) {
    val successColor = SarvaTheme.colors.fitnessSuccess
    val contentColor = SarvaTheme.colors.fitnessContent

    val animatedColor by animateColorAsState(
        targetValue = if (isGoalReached) successColor else contentColor,
        animationSpec = tween(500), label = "color"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000), label = "progress"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(80.dp)
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = animatedColor,
            trackColor = animatedColor.copy(alpha = 0.2f),
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round,
        )

        val iconVector = if (isGoalReached) Icons.Rounded.EmojiEvents else Icons.AutoMirrored.Rounded.DirectionsRun

        Crossfade(targetState = iconVector, label = "icon") { targetIcon ->
            Icon(
                imageVector = targetIcon,
                contentDescription = null,
                tint = animatedColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(name = "Light")
@Composable
private fun PreviewLight() {
    SarvaTheme(darkTheme = false) {
        FitnessWidget(
            steps = HomeState().steps,
            goal = HomeState().stepsGoal,
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@Preview(name = "Dark")
@Composable
private fun PreviewDark() {
    SarvaTheme(darkTheme = true) {
        FitnessWidget(
            steps = HomeState().steps,
            goal = HomeState().stepsGoal,
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}