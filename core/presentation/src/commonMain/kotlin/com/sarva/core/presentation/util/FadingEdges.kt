package com.sarva.core.presentation.util

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.fadingEdges(
    listState: LazyListState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    edgeWidth: Dp = 0.dp,
): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()

        val isVertical = listState.layoutInfo.orientation == Orientation.Vertical
        val edgePx = edgeWidth.toPx()

        val startPaddingPx = contentPadding.calculateStartPadding(layoutDirection).toPx()
        val endPaddingPx = contentPadding.calculateEndPadding(layoutDirection).toPx()
        val topPaddingPx = contentPadding.calculateTopPadding().toPx()
        val bottomPaddingPx = contentPadding.calculateBottomPadding().toPx()

        val showStart =
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        val showEnd =
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != listState.layoutInfo.totalItemsCount - 1

        val startStops = arrayOf(
            0.0f to Color.Transparent,
            0.3f to Color.Transparent,
            0.7f to Color.Black.copy(alpha = 0.5f),
            1.0f to Color.Black
        )

        val endStops = arrayOf(
            0.0f to Color.Black,
            0.3f to Color.Black.copy(alpha = 0.5f),
            0.7f to Color.Transparent,
            1.0f to Color.Transparent
        )

        if (showStart) {
            val startArea = if (isVertical) topPaddingPx else startPaddingPx
            val totalStartEdge = startArea + edgePx

            if (totalStartEdge > 0f) {
                drawRect(
                    brush = if (isVertical) {
                        Brush.verticalGradient(
                            colorStops = startStops,
                            startY = 0f,
                            endY = totalStartEdge
                        )
                    } else {
                        Brush.horizontalGradient(
                            colorStops = startStops,
                            startX = 0f,
                            endX = totalStartEdge
                        )
                    },
                    blendMode = BlendMode.DstIn
                )
            }
        }

        if (showEnd) {
            val endArea = if (isVertical) bottomPaddingPx else endPaddingPx
            val totalEndEdge = endArea + edgePx

            if (totalEndEdge > 0f) {
                drawRect(
                    brush = if (isVertical) {
                        Brush.verticalGradient(
                            colorStops = endStops,
                            startY = size.height - totalEndEdge,
                            endY = size.height
                        )
                    } else {
                        Brush.horizontalGradient(
                            colorStops = endStops,
                            startX = size.width - totalEndEdge,
                            endX = size.width
                        )
                    },
                    blendMode = BlendMode.DstIn
                )
            }
        }
    }