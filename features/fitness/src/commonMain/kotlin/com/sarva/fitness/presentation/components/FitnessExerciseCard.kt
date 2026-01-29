package com.sarva.fitness.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.core.presentation.util.toFormattedDuration
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.fitness.domain.model.FitnessExercise
import kotlinx.datetime.LocalDateTime

@Composable
fun FitnessExerciseCard(
    exercise: FitnessExercise,
    modifier: Modifier = Modifier
) {
    val color = SarvaTheme.colors.fitnessContent

    Column(
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Icon(
                    imageVector = exercise.icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = exercise.type,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = exercise.durationSeconds.toFormattedDuration(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFeatureSettings = "tnum"
                ),
                color = color,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Text(
            text = "${exercise.startTime.time} - ${exercise.endTime.time}",
            style = MaterialTheme.typography.labelMedium,
            color = color.copy(alpha = 0.7f),
            modifier = Modifier.padding(start = 28.dp)
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        FitnessExerciseCard(
            exercise = FitnessExercise(
                id = "1",
                type = "Walking",
                icon = Icons.AutoMirrored.Rounded.DirectionsWalk,
                startTime = LocalDateTime.parse("2023-01-02T23:40"),
                endTime = LocalDateTime.parse("2023-01-02T23:40"),
                durationSeconds = 15
            ),
        )
    }
}
