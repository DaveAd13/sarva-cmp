package com.sarva.app.features.home.presentation.components.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Map
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.app.features.home.presentation.HomeState
import com.sarva.app.generated.resources.Res
import com.sarva.app.generated.resources.cal
import com.sarva.app.generated.resources.connect_apple_health
import com.sarva.app.generated.resources.connect_google_health
import com.sarva.app.generated.resources.connect_health
import com.sarva.app.generated.resources.km
import com.sarva.app.generated.resources.steps
import com.sarva.common.getPlatformName
import com.sarva.core.domain.settings.model.WidgetLayout
import com.sarva.core.presentation.formatting.formatNumber
import com.sarva.core.presentation.generated.resources.ic_steps_24
import com.sarva.designsystem.theme.SarvaTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.sarva.core.presentation.generated.resources.Res as CoreRes

@Composable
fun FitnessWidget(
    state: HomeState,
    onWidgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = state.hasHealthPermission,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { hasPermission ->
            if (hasPermission) {
                FitnessCard(
                    onClick = onWidgetClick,
                    steps = state.steps,
                    goal = state.stepsGoal,
                    distance = state.distance,
                    calories = state.calories,
                    widgetLayout = state.widgetLayout
                )
            } else {
                NeedPermissionCard(onClick = onWidgetClick)
            }
        }
    }
}

@Composable
private fun FitnessCard(
    onClick: () -> Unit,
    steps: Int,
    goal: Int,
    distance: String,
    calories: String,
    widgetLayout: WidgetLayout
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
//        border = BorderStroke(
//            1.dp,
//            MaterialTheme.colorScheme.outline
//        )
    ) {
        if (widgetLayout == WidgetLayout.TILED) {
            CardContentSmall(
                steps = steps,
                goal = goal,
            )
        } else {
            CardContentBig(
                steps = steps,
                goal = goal,
                distance = distance,
                calories = calories
            )
        }
    }
}

@Composable
private fun CardContentSmall(
    steps: Int,
    goal: Int,
) {
    val isGoalReached = steps >= goal
    val progressRaw = (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)

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
            modifier = Modifier
                .padding(bottom = 12.dp)
                .weight(1f)
                .aspectRatio(1f)
        )

        Text(
            text = formatNumber(steps),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "${formatNumber(goal)} ${stringResource(Res.string.steps)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CardContentBig(
    steps: Int,
    goal: Int,
    distance: String,
    calories: String
) {
    val isGoalReached = steps >= goal
    val progressRaw = (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FitnessGauge(
            progress = progressRaw,
            isGoalReached = isGoalReached,
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .aspectRatio(1f)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formatNumber(steps),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = " / ${formatNumber(goal)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    painter = painterResource(CoreRes.drawable.ic_steps_24),
                    contentDescription = null,
                    tint = SarvaTheme.colors.fitness,
                    modifier = Modifier.size(20.dp)
                )
            }

            MetricRow(
                icon = Icons.Rounded.Map,
                text = "$distance ${stringResource(Res.string.km)}",
            )
            MetricRow(
                icon = Icons.Rounded.LocalFireDepartment,
                text = "$calories ${stringResource(Res.string.cal)}",
            )
        }
    }
}

@Composable
private fun NeedPermissionCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
//        border = BorderStroke(
//            1.dp,
//            MaterialTheme.colorScheme.outline
//        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.HealthAndSafety,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = getHealthProviderName(),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun getHealthProviderName(): String {
    return when (getPlatformName()) {
        "Android" -> stringResource(Res.string.connect_google_health)
        "iOS" -> stringResource(Res.string.connect_apple_health)
        else -> stringResource(Res.string.connect_health)
    }
}

@Composable
private fun FitnessGauge(
    progress: Float,
    isGoalReached: Boolean,
    modifier: Modifier,
) {
    val gaugeColor = if (isGoalReached) SarvaTheme.colors.fitnessSuccess else SarvaTheme.colors.fitness

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000)
    )

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val iconSize = minOf(maxWidth, maxHeight) * 0.35f

        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = gaugeColor,
            trackColor = gaugeColor.copy(alpha = 0.2f),
            strokeWidth = (minOf(maxWidth, maxHeight) * 0.08f).coerceAtLeast(4.dp),
            strokeCap = StrokeCap.Round,
        )

        val iconVector =
            if (isGoalReached) Icons.Rounded.EmojiEvents else Icons.AutoMirrored.Rounded.DirectionsRun

        Crossfade(targetState = iconVector, label = "icon") { targetIcon ->
            Icon(
                imageVector = targetIcon,
                contentDescription = null,
                tint = gaugeColor,
                modifier = Modifier
                    .size(iconSize)
            )
        }
    }
}

@Composable
private fun MetricRow(
    icon: ImageVector,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SarvaTheme.colors.fitness,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        FitnessWidget(
            state = HomeState(
                hasHealthPermission = true,
                widgetLayout = WidgetLayout.TILED
            ),
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}