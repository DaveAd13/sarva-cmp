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
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.shimmerSpec

private val DarkColorScheme = darkColorScheme(
    primary = VitaPrimary,
    onPrimary = DarkBackground,
    secondary = VitaSecondary,
    onSecondary = Color.White,
    background = DarkBackground,
    surface = DarkCard, // Default cards use this
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = DarkSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = VitaPrimary,
    onPrimary = Color.White,
    secondary = VitaSecondary,
    onSecondary = Color.White,
    background = LightBackground,
    surface = LightSurface, // Default cards use this
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
)

@Immutable
data class SarvaFeatureColors(
    val noteContainer: Color,
    val noteContent: Color,
    val fitnessContainer: Color,
    val fitnessContent: Color,
    val fitnessSuccess: Color,
    val expenseContainer: Color,
    val expenseContent: Color,
    val expenseCardContainer: Color,
    val taskContainer: Color,
    val taskContent: Color,
    val taskChecked: Color,
    val calendarContainer: Color,
    val calendarContent: Color,
    val placesContainer: Color,
    val placesContent: Color
)

val LocalFeatureColors = staticCompositionLocalOf {
    // Default fallback (usually won't be seen)
    SarvaFeatureColors(
        noteContainer = Color.Unspecified,
        noteContent = Color.Unspecified,
        fitnessContainer = Color.Unspecified,
        fitnessContent = Color.Unspecified,
        fitnessSuccess = Color.Unspecified,
        expenseContainer = Color.Unspecified,
        expenseContent = Color.Unspecified,
        expenseCardContainer = Color.Unspecified,
        taskContainer = Color.Unspecified,
        taskContent = Color.Unspecified,
        taskChecked = Color.Unspecified,
        calendarContainer = Color.Unspecified,
        calendarContent = Color.Unspecified,
        placesContainer = Color.Unspecified,
        placesContent = Color.Unspecified
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
    content: @Composable() () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val featureColors = if (darkTheme) {
        SarvaFeatureColors(
            noteContainer = NoteDarkBg,
            noteContent = NoteDarkContent,
            fitnessContainer = FitnessDarkBg,
            fitnessContent = FitnessDarkContent,
            fitnessSuccess = FitnessDarkSuccess,
            expenseContainer = ExpenseDarkBg,
            expenseContent = ExpenseDarkContent,
            expenseCardContainer = ExpenseCardDarkBg,
            taskContainer = TaskDarkBg,
            taskContent = TaskDarkContent,
            taskChecked = TaskDarkChecked,
            calendarContainer = CalendarDarkBg,
            calendarContent = CalendarDarkContent,
            placesContainer = PlacesDarkBg,
            placesContent = PlacesDarkContent
        )
    } else {
        SarvaFeatureColors(
            noteContainer = NoteLightBg,
            noteContent = NoteLightContent,
            fitnessContainer = FitnessLightBg,
            fitnessContent = FitnessLightContent,
            fitnessSuccess = FitnessLightSuccess,
            expenseContainer = ExpenseLightBg,
            expenseCardContainer = ExpenseCardLightBg,
            expenseContent = ExpenseLightContent,
            taskContainer = TaskLightBg,
            taskContent = TaskLightContent,
            taskChecked = TaskLightChecked,
            calendarContainer = CalendarLightBg,
            calendarContent = CalendarLightContent,
            placesContainer = PlacesLightBg,
            placesContent = PlacesLightContent
        )
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
