package com.sarva.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.sarva.app.generated.resources.Res
import com.sarva.app.generated.resources.home
import com.sarva.app.generated.resources.more
import com.sarva.core.presentation.util.UiText

data class BottomNavItem(
    val label: UiText,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val contentDescription: UiText
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    Route.Home to BottomNavItem(
        label = UiText.StringRes(Res.string.home),
        unselectedIcon = Icons.Default.Home,
        selectedIcon = Icons.Filled.Home,
        contentDescription = UiText.StringRes(Res.string.home)
    ),
    Route.More to BottomNavItem(
        label = UiText.StringRes(Res.string.more),
        unselectedIcon = Icons.Default.MoreHoriz,
        selectedIcon = Icons.Filled.MoreHoriz,
        contentDescription = UiText.StringRes(Res.string.more)
    ),
)