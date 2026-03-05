package com.sarva.fitness.presentation.activity_history.components.history_chart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.sarva.fitness.domain.model.ChartTransition
import com.sarva.fitness.domain.model.ChartUiData
import com.sarva.fitness.domain.model.FitnessActivity
import com.sarva.fitness.domain.model.FitnessRecordType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

@Composable
fun AnimatedFitnessChartContainer(
    data: ChartUiData,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = data,
        transitionSpec = {

            when (data.transition) {
                ChartTransition.FORWARD -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400)
                    ) + fadeIn(tween(400)) togetherWith
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            ) + fadeOut(tween(400))
                }

                ChartTransition.BACKWARD -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(400)
                    ) + fadeIn(tween(400)) togetherWith
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            ) + fadeOut(tween(400))
                }

                ChartTransition.DEFAULT -> {
                    (fadeIn(animationSpec = tween(300)) +
                            scaleIn(initialScale = 0.95f, animationSpec = tween(300)))
                        .togetherWith(
                            fadeOut(animationSpec = tween(300))
                        )
                }
            }
        },
        label = "ChartAnimation"
    ) { targetData ->
        FitnessChart(
            data = targetData,
            modifier = modifier
        )
    }
}

@Composable
fun FitnessChart(
    data: ChartUiData,
    modifier: Modifier = Modifier,
) {
    val contentColor = SarvaTheme.colors.fitness
    val gridLineColor = contentColor.copy(alpha = 0.2f)
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 11.sp,
        color = contentColor,
    )

    val barCountIdentity = remember(data.bars.size) { Any() }
    val barProgress = remember(barCountIdentity) { Animatable(0f) }

    LaunchedEffect(barCountIdentity) {
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

        val labelAreaWidth = 45.dp.toPx()
        val labelPadding = 8.dp.toPx()
        val chartWidth = w - labelAreaWidth
        val lineStroke = 1.dp.toPx()

        // Draw Grid Lines
        drawLine(gridLineColor, Offset(0f, 0f), Offset(chartWidth, 0f), lineStroke)
        drawLine(gridLineColor, Offset(0f, h / 2), Offset(chartWidth, h / 2), lineStroke)
        drawLine(gridLineColor, Offset(0f, h), Offset(chartWidth, h), strokeWidth = 2.dp.toPx())

        // Labels
        val labels = listOf(
            formatNumber(data.maxRange.toInt()) to 0f,
            formatNumber((data.maxRange / 2).toInt()) to h / 2
        )

        labels.forEach { (text, y) ->
            val layout = textMeasurer.measure(text, textStyle)
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(
                    x = chartWidth + labelPadding,
                    y = y - (layout.size.height / 2)
                )
            )
        }

        val barCount = data.bars.size
        if (barCount == 0) return@Canvas

        val spacing = if (barCount > 20) 4.dp.toPx() else 12.dp.toPx()
        val totalSpacing = spacing * (barCount - 1)
        val barWidth = (chartWidth - totalSpacing) / barCount
        val cornerRadius = CornerRadius(2.dp.toPx())

        data.bars.forEachIndexed { index, bar ->
            val x = index * (barWidth + spacing)
            val normalizedValue = if (data.maxRange > 0) bar.value / data.maxRange else 0f
            val heightRatio = normalizedValue * barProgress.value
            val barHeight = h * heightRatio

            if (barHeight > 0) {
                drawRoundRect(
                    color = contentColor,
                    topLeft = Offset(x, h - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = cornerRadius
                )
            }

            if (bar.label.isNotEmpty()) {
                val labelLayout = textMeasurer.measure(bar.label, textStyle)
                drawText(
                    textLayoutResult = labelLayout,
                    topLeft = Offset(
                        x = x + (barWidth / 2) - (labelLayout.size.width / 2),
                        y = h + 6.dp.toPx()
                    )
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
        AnimatedFitnessChartContainer(
            data = rememberChartData(
                fitnessActivity = FitnessActivity(persistentListOf(), persistentListOf()),
                period = ActivityPeriod.DAY,
                anchorDate = LocalDate(2023, 1, 1),
                recordType = FitnessRecordType.STEPS,
                transition = ChartTransition.DEFAULT
            ),
            modifier = Modifier
                .background(color = SarvaTheme.colors.fitness)
                .padding(16.dp)
        )
    }
}
