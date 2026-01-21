package com.sarva.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sarva.app.features.home.presentation.HomeNavigationAction
import com.sarva.app.features.home.presentation.HomeRoot
import com.sarva.designsystem.theme.Transparent
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun MainRoot(
    onNavigate: (HomeNavigationAction) -> Unit,
) {
    MainScreen(
        onNavigate = onNavigate,
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigate: (HomeNavigationAction) -> Unit,
) {
    val hazeState = rememberHazeState()

    val navigationState = rememberNavigationState(
        startRoute = Route.Home,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys,
    )

    val navigator = remember { Navigator(navigationState) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val titleText =
                        TOP_LEVEL_DESTINATIONS[navigationState.topLevelRoute]?.label?.asStringC()
                            ?: navigationState.topLevelRoute.toString()
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeStyle.Unspecified.copy(blurRadius = 15.dp)
                    )
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(Transparent),
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeStyle(
                            blurRadius = 15.dp,
                            tints = listOf(HazeMaterials.ultraThin().tints.first())
                        )
                    ),
                containerColor = Transparent
            ) {

                TOP_LEVEL_DESTINATIONS.forEach { (topLevelDestination, data) ->
                    val isSelected = topLevelDestination == navigationState.topLevelRoute
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navigator.navigate(topLevelDestination)
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) data.selectedIcon else data.unselectedIcon,
                                contentDescription = data.contentDescription.asStringC(),
                                modifier = Modifier
                                    .size(32.dp),
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.5f
                            ),
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavDisplay(
            onBack = navigator::goBack,
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Home> {
                        HomeRoot(
                            onNavigate = onNavigate,
                            hazeState = hazeState,
                            contentPadding = padding,
                        )
                    }
                    entry<Route.More> {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { Text("More Options") }
                    }
                },
            )
        )
    }
}