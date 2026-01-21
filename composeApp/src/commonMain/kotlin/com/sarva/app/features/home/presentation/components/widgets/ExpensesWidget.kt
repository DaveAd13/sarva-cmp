package com.sarva.app.features.home.presentation.components.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.app.features.home.domain.model.SpentInfo
import com.sarva.app.features.home.presentation.HomeState


@Composable
fun ExpensesWidget(
    spentInfo: SpentInfo,
    onWidgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = SarvaTheme.colors.expenseContainer
    val contentColor = SarvaTheme.colors.expenseContent

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onWidgetClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AttachMoney,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${spentInfo.currency}${spentInfo.totalSpent.toInt()}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = contentColor
                    )
                    Text(
                        text = "Spent Today",
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (spentInfo.recentSpendingTrend.isNotEmpty()) {
                    MiniBarChart(
                        dataPoints = spentInfo.recentSpendingTrend,
                        barColor = contentColor
                    )
                } else {
                    Text(
                        text = "No recent data",
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniBarChart(
    dataPoints: List<Float>,
    barColor: Color
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val barWidth = size.width / (dataPoints.size * 1.5f)
        val spacing = barWidth / 2f
        val maxBarHeight = size.height

        val totalWidth = (barWidth * dataPoints.size) + (spacing * (dataPoints.size - 1))
        val startX = (size.width - totalWidth) / 2f

        dataPoints.forEachIndexed { index, value ->
            val barHeight =
                maxBarHeight * value.coerceIn(0.1f, 1f)
            val x = startX + (index * (barWidth + spacing))
            val y = size.height - barHeight

            drawRoundRect(
                color = if (index == dataPoints.lastIndex) barColor else barColor.copy(alpha = 0.5f),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
        }
    }
}

@Preview(name = "Light")
@Composable
private fun PreviewLight() {
    SarvaTheme(darkTheme = false) {
        ExpensesWidget(
            spentInfo = HomeState().spentInfo,
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@Preview(name = "Dark")
@Composable
private fun PreviewDark() {
    SarvaTheme(darkTheme = true) {
        ExpensesWidget(
            spentInfo = HomeState().spentInfo,
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}