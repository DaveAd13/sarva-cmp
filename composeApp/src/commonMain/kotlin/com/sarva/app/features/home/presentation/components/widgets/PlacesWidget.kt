package com.sarva.app.features.home.presentation.components.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.designsystem.theme.SarvaTheme

@Composable
fun PlacesWidget(
    onWidgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val containerColor = SarvaTheme.colors.placesContainer
    val contentColor = SarvaTheme.colors.placesContent

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
            verticalArrangement = Arrangement.SpaceBetween
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
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Places",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = contentColor.copy(alpha = 0.9f)
                )
            }

            Column {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor.copy(alpha = 0.7f)
                )
                Text(
                    text = "Nearby Spots",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = contentColor
                )
            }

            Button(
                onClick = onWidgetClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = contentColor,
                    contentColor = containerColor
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Open Map",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(name = "Light")
@Composable
private fun PreviewLight() {
    SarvaTheme(darkTheme = false) {
        PlacesWidget(
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@Preview(name = "Dark")
@Composable
private fun PreviewDark() {
    SarvaTheme(darkTheme = true) {
        PlacesWidget(
            onWidgetClick = {},
            modifier = Modifier.aspectRatio(1f)
        )
    }
}