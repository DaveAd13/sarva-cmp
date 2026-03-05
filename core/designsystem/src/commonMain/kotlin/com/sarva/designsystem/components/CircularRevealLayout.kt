package com.sarva.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.yield
import kotlin.math.hypot

@Composable
fun CircularRevealLayout(
    isDark: Boolean,
    content: @Composable (isDark: Boolean) -> Unit
) {
    var animatingIsDark by remember { mutableStateOf(isDark) }
    var targetIsDark by remember { mutableStateOf(isDark) }
    var isInitialized by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    val animProgress = remember { Animatable(0f) }

    val density = LocalDensity.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = constraints.maxWidth.toFloat()
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val rightInset = WindowInsets.displayCutout.asPaddingValues()
            .calculateRightPadding(LocalLayoutDirection.current)


        val revealFrom = remember(screenWidth, statusBarHeight, rightInset) {
            with(density) {
                Offset(
                    x = screenWidth - rightInset.toPx() - 24.dp.toPx(),
                    y = statusBarHeight.toPx() + 32.dp.toPx()
                )
            }
        }

        LaunchedEffect(isDark) {
            if (!isInitialized) {
                animatingIsDark = isDark
                targetIsDark = isDark
                isInitialized = true
                return@LaunchedEffect
            }

            if (isDark != animatingIsDark) {
                targetIsDark = isDark
                isAnimating = true
                animProgress.snapTo(0f)
                animProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 759, easing = FastOutSlowInEasing),
                )

                animatingIsDark = isDark
                yield()
                isAnimating = false
            }
        }

        val transitioningToDark = targetIsDark && !animatingIsDark

        Box(modifier = Modifier.fillMaxSize()) {
            content(if (transitioningToDark) animatingIsDark else targetIsDark)

            if (isAnimating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                            clip = true
                            shape = object : Shape {
                                override fun createOutline(
                                    size: Size,
                                    layoutDirection: LayoutDirection,
                                    density: Density
                                ): Outline {
                                    val maxRadius = hypot(size.width, size.height) * 1.5f
                                    val radius = if (transitioningToDark) {
                                        maxRadius * animProgress.value
                                    } else {
                                        maxRadius * (1f - animProgress.value)
                                    }

                                    return Outline.Generic(Path().apply {
                                        addOval(Rect(center = revealFrom, radius = radius))
                                    })
                                }
                            }
                        }
                ) {
                    content(if (transitioningToDark) targetIsDark else animatingIsDark)
                }
            }
        }
    }
}