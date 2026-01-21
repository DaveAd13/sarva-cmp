package com.sarva.app.features.home.presentation.components.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.app.features.home.presentation.HomeState
import com.sarva.app.features.tasks.domain.model.Task
import com.sarva.designsystem.theme.SarvaTheme

@Composable
fun TaskWidget(
    tasks: List<Task>,
    onTaskToggle: (String) -> Unit,
    onWidgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = SarvaTheme.colors.taskContainer
    val contentColor = SarvaTheme.colors.taskContent
    val checkedColor = SarvaTheme.colors.taskChecked

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onWidgetClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.FormatListBulleted,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "${tasks.count { !it.isCompleted }} Remaining",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = contentColor.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                if (tasks.isEmpty()) {
                    Text(
                        text = "All caught up!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.6f)
                    )
                } else {
                    tasks.take(3).forEach { task ->
                        TaskItem(
                            task = task,
                            contentColor = contentColor,
                            checkedColor = checkedColor,
                            onToggle = { onTaskToggle(task.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    contentColor: Color,
    checkedColor: Color,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .clickable(
                onClick = onToggle,
            )
    ) {
        val checkboxModifier = if (task.isCompleted) {
            Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(checkedColor)
        } else {
            Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .border(2.dp, contentColor.copy(alpha = 0.3f), CircleShape)
        }

        Box(
            modifier = checkboxModifier,
            contentAlignment = Alignment.Center
        ) {
            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (task.isCompleted) contentColor.copy(alpha = 0.5f) else contentColor
        )
    }
}

@Preview(name = "Light")
@Composable
private fun PreviewLight() {
    SarvaTheme(darkTheme = false) {
        TaskWidget(
            tasks = HomeState().tasks,
            onTaskToggle = {},
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@Preview(name = "Dark")
@Composable
private fun PreviewDark() {
    SarvaTheme(darkTheme = true) {
        TaskWidget(
            tasks = HomeState().tasks,
            onTaskToggle = {},
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}