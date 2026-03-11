package com.sarva.fitness.presentation.activity_history.components.history_chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sarva.core.presentation.formatting.formatNumber
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.fitness.domain.model.ActivityPeriod
import com.sarva.fitness.domain.model.ChartUiData
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.FitnessRecordType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun FitnessChart(
    data: ChartUiData,
    isLoading: Boolean,
    accentColor: Color = SarvaTheme.colors.fitness,
    modifier: Modifier = Modifier,
) {
    val loadingColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    val gridLineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurface,
    )
    val rectPath = Path()

    var displayedData by remember { mutableStateOf(data) }
    var showLoadingAnimation by remember { mutableStateOf(false) }

    val barProgress = remember { Animatable(0f) }
    val entryScale = remember { Animatable(0f) }

    val animatedGoalRatio by animateFloatAsState(
        targetValue = (displayedData.stepGoal / displayedData.maxRange).coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (displayedData.showGoalLine) 0.5f else 0f,
        animationSpec = tween(durationMillis = 400),
    )

    val infiniteTransition = rememberInfiniteTransition()
    val waveValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
    )

    LaunchedEffect(data, isLoading) {
        if (barProgress.value > 0f) {
            barProgress.animateTo(0f, tween(200, easing = LinearOutSlowInEasing))
        }

        if (isLoading) {
            showLoadingAnimation = true
            entryScale.snapTo(0f)
            entryScale.animateTo(1f, tween(400, easing = EaseOutQuart))
            snapshotFlow { isLoading }.first { !it }
        }

        displayedData = data
        showLoadingAnimation = false
        barProgress.animateTo(1f, tween(400, easing = LinearOutSlowInEasing))
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 16.dp, bottom = 12.dp)
            .padding(start = 20.dp, end = 10.dp, bottom = 10.dp)
    ) {
        val w = size.width
        val h = size.height
        val chartWidth = w - 45.dp.toPx()
        val goalRatio = (displayedData.stepGoal / displayedData.maxRange).coerceIn(0f, 1f)

        //top gridLine and label
        drawLine(gridLineColor, Offset(0f, 0f), Offset(chartWidth, 0f), 1.dp.toPx())
        val isCollidingWithTop = displayedData.showGoalLine && goalRatio > 0.9f
        if (!isCollidingWithTop) {
            val topLabel = if(isLoading) formatNumber(displayedData.maxRange.toInt()) else formatNumber(data.maxRange.toInt())
            val textLayoutTop =
                textMeasurer.measure(topLabel, textStyle)
            drawText(
                textLayoutResult = textLayoutTop,
                topLeft = Offset(
                    chartWidth + 8.dp.toPx(),
                    -(textLayoutTop.size.height / 2).toFloat()
                )
            )
        }

        //mid gridLine and label
        val isCollidingWithMid = abs(goalRatio - 0.5f) < 0.15f
        if (!isCollidingWithMid) {
            val midH = h / 2
            val midLabelY = if(isLoading) formatNumber((displayedData.maxRange / 2).toInt()) else formatNumber((data.maxRange / 2).toInt())
            drawLine(gridLineColor, Offset(0f, midH), Offset(chartWidth, midH), 1.dp.toPx())
            val textLayoutMid =
                textMeasurer.measure(midLabelY, textStyle)
            drawText(
                textLayoutResult = textLayoutMid,
                topLeft = Offset(
                    chartWidth + 8.dp.toPx(),
                    midH - (textLayoutMid.size.height / 2).toFloat()
                )
            )
        }

        //X axis
        drawLine(
            gridLineColor,
            Offset(0f, h + 1.dp.toPx()),
            Offset(chartWidth, h + 1.dp.toPx()),
            2.dp.toPx()
        )

        val targetBars = data.bars
        val targetSpacing = if (targetBars.size > 20) 4.dp.toPx() else 12.dp.toPx()
        val targetBarWidth =
            (chartWidth - (targetSpacing * (targetBars.size - 1))) / targetBars.size

        targetBars.forEachIndexed { index, bar ->
            val x = index * (targetBarWidth + targetSpacing)

            //X axis labels
            if (bar.label.isNotEmpty()) {
                val labelLayout = textMeasurer.measure(bar.label, textStyle)
                drawText(
                    textLayoutResult = labelLayout,
                    topLeft = Offset(
                        x = x + (targetBarWidth / 2) - (labelLayout.size.width / 2),
                        y = h + 6.dp.toPx()
                    ),
                )
            }

            //loading bars
            if (showLoadingAnimation && barProgress.value == 0f) {
                val variation = (sin(waveValue + index * 0.4f) + 1f) / 2f
                val baseHeight = h * (0.15f + (variation * 0.2f))

                val individualProgress = (entryScale.value * 2f - (index * 0.05f)).coerceIn(0f, 1f)
                val equalizerHeight = baseHeight * individualProgress

                val rectPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = x,
                            top = h - equalizerHeight,
                            right = x + targetBarWidth,
                            bottom = h,
                            topLeftCornerRadius = CornerRadius(4.dp.toPx()),
                            topRightCornerRadius = CornerRadius(4.dp.toPx()),
                            bottomRightCornerRadius = CornerRadius(0f),
                            bottomLeftCornerRadius = CornerRadius(0f)
                        )
                    )
                }
                drawPath(path = rectPath, color = loadingColor)
            }
        }

        //goal line and label
        if (animatedAlpha > 0f) {
            val goalY = h - (h * animatedGoalRatio)
            val dashSize = 15f
            val baseGap = 10f
            val patternSum = dashSize + baseGap
            val count = ((chartWidth + baseGap) / patternSum).toInt().coerceAtLeast(1)
            val dif = (chartWidth + baseGap) % patternSum
            val finalGapSize = baseGap + (dif / count)

            drawLine(
                accentColor.copy(alpha = animatedAlpha),
                Offset(0f, goalY),
                Offset(chartWidth, goalY),
                1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(dashSize, finalGapSize),
                )
            )

            val goalLayout = textMeasurer.measure(
                formatNumber(displayedData.stepGoal),
                textStyle.copy(color = accentColor.copy(alpha = animatedAlpha * 2))
            )

            drawText(
                textLayoutResult = goalLayout,
                topLeft = Offset(chartWidth + 8.dp.toPx(), goalY - (goalLayout.size.height / 2))
            )
        }

        //chart bars
        val bars = displayedData.bars
        if (bars.isNotEmpty()) {
            val currentSpacing = if (bars.size > 20) 4.dp.toPx() else 12.dp.toPx()
            val currentBarWidth = (chartWidth - (currentSpacing * (bars.size - 1))) / bars.size

            bars.forEachIndexed { index, bar ->
                val x = index * (currentBarWidth + currentSpacing)
                val normalizedValue =
                    if (displayedData.maxRange > 0) bar.value / displayedData.maxRange else 0f

                val barHeight = h * normalizedValue * barProgress.value

                if (barHeight > 0) {
                    rectPath.reset()
                    rectPath.addRoundRect(
                        roundRect = RoundRect(
                            left = x,
                            top = h - barHeight,
                            right = x + currentBarWidth,
                            bottom = h,
                            topLeftCornerRadius = CornerRadius(4.dp.toPx()),
                            topRightCornerRadius = CornerRadius(4.dp.toPx()),
                            bottomRightCornerRadius = CornerRadius(0f),
                            bottomLeftCornerRadius = CornerRadius(0f)
                        )
                    )
                    drawPath(path = rectPath, color = accentColor)
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
        FitnessChart(
            data = rememberChartData(
                fitnessActivity = FitnessActivity(persistentListOf(), persistentListOf()),
                period = ActivityPeriod.DAY,
                anchorDate = LocalDate(2023, 1, 1),
                recordType = FitnessRecordType.STEPS,
            ),
            isLoading = false,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        )
    }
}