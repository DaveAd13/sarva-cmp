package com.sarva.features.location.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.sarva.core.domain.model.location.LocationSearchResult
import com.sarva.core.presentation.textfields.ClearTextIcon
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.features.location.generated.resources.Res
import com.sarva.features.location.generated.resources.search_places
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LocationSearchRoot(
    onLocationSelected: (LocationSearchResult) -> Unit,
    onDismiss: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    viewModel: LocationSearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LocationSearchEvent.LocationSelected -> {
                onLocationSelected(event.location)
                onDismiss()
            }
        }
    }

    val backState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)

    NavigationBackHandler(
        state = backState,
        isBackEnabled = true,
        onBackCompleted = { onDismiss() }
    )

    LocationSearchScreen(
        state = state,
        onAction = viewModel::onAction,
        onDismiss = onDismiss,
        containerColor = containerColor,
        contentColor = contentColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(
    state: LocationSearchState,
    onAction: (LocationSearchAction) -> Unit,
    onDismiss: () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        selectionColors = TextSelectionColors(
            handleColor = contentColor,
            backgroundColor = contentColor.copy(alpha = 0.2f)
        ),
        cursorColor = contentColor,
    )
    val focusRequester = remember { FocusRequester() }


    Scaffold(
        containerColor = containerColor,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    TextField(
                        state = state.searchTextFieldState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        lineLimits = TextFieldLineLimits.SingleLine,
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.search_places),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = contentColor.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Medium,
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        },
                        trailingIcon = {
                            if (state.isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(14.dp),
                                    strokeWidth = 2.dp,
                                    color = contentColor
                                )
                            } else {
                                ClearTextIcon(color = contentColor) {
                                    if (state.searchTextFieldState.text.isNotEmpty()) {
                                        state.searchTextFieldState.clearText()
                                    } else {
                                        onDismiss()
                                    }
                                }
                            }
                        },
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            color = contentColor,
                            fontWeight = FontWeight.Normal,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        colors = textFieldColors,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerColor,
                    scrolledContainerColor = containerColor,
                    titleContentColor = contentColor,
                    navigationIconContentColor = contentColor,
                    actionIconContentColor = contentColor
                ),
            )
        },
    ) { contentPadding ->
        if (state.results.isEmpty() && !state.isSearching && state.searchTextFieldState.text.length >= 3) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = contentColor.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "No places found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .imePadding(),
            ) {
                items(state.results) { location ->
                    LocationResultItem(
                        location = location,
                        contentColor = contentColor,
                        onClick = { onAction(LocationSearchAction.OnLocationSelected(location)) }
                    )
                    HorizontalDivider(
                        color = contentColor.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun LocationResultItem(
    location: LocationSearchResult,
    contentColor: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = location.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = contentColor,
                    fontWeight = FontWeight.Medium,
                ),
            )
            location.city?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = contentColor.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        Surface {
            LocationSearchScreen(
                state = LocationSearchState(
                    results = listOf(
                        LocationSearchResult(
                            name = "Pizza",
                            city = "New York",
                            country = "USA",
                            latitude = 0.0,
                            longitude = 0.0
                        ),
                        LocationSearchResult(
                            name = "Kond",
                            city = "Yerevan",
                            country = "Armenia",
                            latitude = 0.0,
                            longitude = 0.0
                        ),
                        LocationSearchResult(
                            name = "Tawouk",
                            city = "Yerevan",
                            country = "Armenia",
                            latitude = 0.0,
                            longitude = 0.0
                        ),
                    )
                ),
                onAction = {},
                onDismiss = {},
                containerColor = SarvaTheme.colors.expenseContainer,
                contentColor = SarvaTheme.colors.expenseContent,
            )
        }
    }
}