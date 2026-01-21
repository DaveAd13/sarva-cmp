package com.sarva.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp
import com.sarva.core.designsystem.generated.resources.Res
import com.sarva.core.designsystem.generated.resources.inter_medium
import com.sarva.core.designsystem.generated.resources.inter_regular
import com.sarva.core.designsystem.generated.resources.inter_semibold
import com.sarva.core.designsystem.generated.resources.poppins_medium
import com.sarva.core.designsystem.generated.resources.poppins_semibold
import org.jetbrains.compose.resources.Font

val Inter
    @Composable get() = FontFamily(
        Font(resource = Res.font.inter_regular, weight = FontWeight.Normal),
        Font(resource = Res.font.inter_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.inter_semibold, weight = FontWeight.SemiBold),
    )

val Poppins
    @Composable get() = FontFamily(
        Font(resource = Res.font.poppins_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.poppins_semibold, weight = FontWeight.SemiBold),
    )

val SarvaTypography: Typography
    @Composable get() = Typography(
        displayLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        displayMedium = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 34.sp
        ),
        displaySmall = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 30.sp
        ),

        titleLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 26.sp
        ),
        titleMedium = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),

        // Body (Inter)
        bodyLarge = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp
        ),

        // Labels / Buttons (Inter)
        labelLarge = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        ),
        labelMedium = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        ),
        labelSmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    )
