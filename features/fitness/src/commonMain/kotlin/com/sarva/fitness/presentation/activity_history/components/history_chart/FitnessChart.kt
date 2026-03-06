package com.sarva.fitness.presentation.activity_history.components.history_chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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

    var displayedData by remember { mutableStateOf(data) }
    var showLoadingAnimation by remember { mutableStateOf(false) }

    val barProgress = remember { Animatable(0f) }
    val entryScale = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "loading_pulse")
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
            barProgress.animateTo(0f, tween(250, easing = LinearOutSlowInEasing))
        }

        if (isLoading) {
            showLoadingAnimation = true
            entryScale.snapTo(0f)
            entryScale.animateTo(1f, tween(600, easing = EaseOutQuart))
            snapshotFlow { isLoading }.first { !it }
        }

        displayedData = data
        showLoadingAnimation = false
        barProgress.animateTo(1f, tween(500, easing = LinearOutSlowInEasing))
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


        drawLine(gridLineColor, Offset(0f, 0f), Offset(chartWidth, 0f), 1.dp.toPx())
        drawLine(gridLineColor, Offset(0f, h / 2), Offset(chartWidth, h / 2), 1.dp.toPx())
        drawLine(gridLineColor, Offset(0f, h), Offset(chartWidth, h), 2.dp.toPx())

        val labels = listOf(
            formatNumber(data.maxRange.toInt()) to 0f,
            formatNumber((data.maxRange / 2).toInt()) to h / 2
        )

        labels.forEach { (text, y) ->
            val layout = textMeasurer.measure(text, textStyle)
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(
                    x = chartWidth + 8.dp.toPx(),
                    y = y - (layout.size.height / 2)
                )
            )
        }

        val bars = displayedData.bars
        if (bars.isEmpty()) return@Canvas

        val spacing = if (bars.size > 20) 4.dp.toPx() else 12.dp.toPx()
        val barWidth = (chartWidth - (spacing * (bars.size - 1))) / bars.size

        bars.forEachIndexed { index, bar ->
            val x = index * (barWidth + spacing)

            if (showLoadingAnimation && barProgress.value == 0f) {
                val variation = (sin(waveValue + index * 0.4f) + 1f) / 2f
                val baseHeight = h * (0.15f + (variation * 0.2f))

                val individualProgress = (entryScale.value * 2f - (index * 0.05f)).coerceIn(0f, 1f)
                val equalizerHeight = baseHeight * individualProgress

                drawRoundRect(
                    color = loadingColor,
                    topLeft = Offset(x, h - equalizerHeight),
                    size = Size(barWidth, equalizerHeight),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            } else {
                val normalizedValue =
                    if (displayedData.maxRange > 0) bar.value / displayedData.maxRange else 0f

                val barHeight = h * normalizedValue * barProgress.value

                if (barHeight > 0) {
                    drawRoundRect(
                        color = accentColor,
                        topLeft = Offset(x, h - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(2.dp.toPx())
                    )
                }

                if (bar.label.isNotEmpty()) {
                    val labelLayout = textMeasurer.measure(bar.label, textStyle)
                    drawText(
                        textLayoutResult = labelLayout,
                        topLeft = Offset(
                            x = x + (barWidth / 2) - (labelLayout.size.width / 2),
                            y = h + 6.dp.toPx()
                        ),
                        alpha = barProgress.value
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
        FitnessChart(
            data = rememberChartData(
                fitnessActivity = FitnessActivity(persistentListOf(), persistentListOf()),
                period = ActivityPeriod.DAY,
                anchorDate = LocalDate(2023, 1, 1),
                recordType = FitnessRecordType.STEPS,
            ),
            isLoading = false,
            modifier = Modifier
                .background(color = SarvaTheme.colors.fitness)
                .padding(16.dp)
        )
    }
}