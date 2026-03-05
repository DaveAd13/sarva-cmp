package com.sarva.designsystem.theme

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.shimmerSpec

private val DarkColorScheme = darkColorScheme(
    primary = SarvaPrimary,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurface.copy(alpha = 0.7f),
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = SarvaPrimary,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurface.copy(alpha = 0.7f),
    outline = LightOutline
)

@Immutable
data class SarvaFeatureColors(
    val fitness: Color,
    val fitnessSuccess: Color,
    val expenses: Color,
    val notes: Color,
    val tasks: Color,
    val calendar: Color,
    val places: Color
)

val LocalFeatureColors = staticCompositionLocalOf {
    SarvaFeatureColors(
        fitness = Color.Unspecified,
        fitnessSuccess = Color.Unspecified,
        expenses = Color.Unspecified,
        notes = Color.Unspecified,
        tasks = Color.Unspecified,
        calendar = Color.Unspecified,
        places = Color.Unspecified
    )
}

val sarvaShimmerTheme = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = shimmerSpec(
            durationMillis = 1000,
            delayMillis = 300
        ),
        repeatMode = RepeatMode.Restart
    ),
)

@Composable
fun SarvaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val featureColors = remember(darkTheme) {
        if (darkTheme) {
            SarvaFeatureColors(
                fitness = DarkAccentFitness,
                fitnessSuccess = DarkAccentFitnessSuccess,
                expenses = DarkAccentExpenses,
                notes = DarkAccentNotes,
                tasks = DarkAccentTasks,
                calendar = DarkAccentCalendar,
                places = DarkAccentPlaces
            )
        } else {
            SarvaFeatureColors(
                fitness = LightAccentFitness,
                fitnessSuccess = LightAccentFitnessSuccess,
                expenses = LightAccentExpenses,
                notes = LightAccentNotes,
                tasks = LightAccentTasks,
                calendar = LightAccentCalendar,
                places = LightAccentPlaces
            )
        }
    }

    CompositionLocalProvider(LocalFeatureColors provides featureColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SarvaTypography,
            content = content
        )
    }
}

object SarvaTheme {
    val colors: SarvaFeatureColors
        @Composable
        get() = LocalFeatureColors.current
}
