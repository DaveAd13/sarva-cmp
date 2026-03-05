package com.sarva.core.presentation.currency_picker.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.sarva.core.domain.currencies.model.Currency
import com.sarva.core.presentation.generated.resources.Res
import com.sarva.core.presentation.generated.resources.all_currencies
import com.sarva.core.presentation.generated.resources.flag_placeholder
import com.sarva.core.presentation.generated.resources.recent
import com.sarva.core.presentation.generated.resources.search_currencies
import com.sarva.core.presentation.textfields.ClearTextIcon
import com.sarva.core.presentation.util.FlagRegistry
import com.sarva.core.presentation.util.ObserveAsEvents
import com.sarva.designsystem.theme.SarvaTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CurrencyPickerRoot(
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit,
    accentColor: Color = SarvaTheme.colors.expenses,
    viewModel: CurrencyPickerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CurrencyPickerEvent.CurrencySelected -> {
                onCurrencySelected(event.currency)
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

    CurrencyPickerScreen(
        state = state,
        onAction = viewModel::onAction,
        onDismiss = onDismiss,
        accentColor = accentColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerScreen(
    state: CurrencyPickerState,
    onAction: (CurrencyPickerAction) -> Unit,
    onDismiss: () -> Unit = {},
    accentColor: Color = SarvaTheme.colors.expenses
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        selectionColors = TextSelectionColors(
            handleColor = accentColor, // Cursor handle uses accent
            backgroundColor = accentColor.copy(alpha = 0.2f)
        ),
        cursorColor = accentColor, // Cursor uses accent
    )
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        // Scaffold uses the global background color
        containerColor = MaterialTheme.colorScheme.background,
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
                                text = stringResource(Res.string.search_currencies),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Semantic hint color
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
                                    color = accentColor // Progress uses accent
                                )
                            } else {
                                ClearTextIcon(color = MaterialTheme.colorScheme.onBackground) {
                                    if (state.searchTextFieldState.text.isNotEmpty()) {
                                        state.searchTextFieldState.clearText()
                                    } else {
                                        onDismiss()
                                    }
                                }
                            }
                        },
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Normal,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        colors = textFieldColors,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        },
    ) { contentPadding ->
        if (state.currencies.isEmpty() && !state.isSearching && state.searchTextFieldState.text.length >= 3) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "No currencies found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                if (state.recentCurrencies.isNotEmpty()) {
                    item { SectionHeader(text = stringResource(Res.string.recent)) }
                    items(
                        items = state.recentCurrencies,
                        key = { "recent_${it.code}" },
                        contentType = { "currency_row" }
                    ) { currency ->
                        CurrencyRow(currency = currency, onAction = onAction)
                    }
                }

                item { SectionHeader(text = stringResource(Res.string.all_currencies)) }
                items(
                    items = state.currencies,
                    key = { "all_${it.code}" },
                    contentType = { "currency_row" }
                ) { currency ->
                    CurrencyRow(currency = currency, onAction = onAction)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun SectionHeader(text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant // Role-based color
            )
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun CurrencyRow(
    currency: Currency,
    onAction: (CurrencyPickerAction) -> Unit,
) {
    val flagResource = remember(currency.code) {
        FlagRegistry.getFlag(currency.code)
    }

    ListItem(
        headlineContent = {
            Text(
                text = currency.code,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface, // Unified Row text
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        modifier = Modifier.clickable {
            onAction(CurrencyPickerAction.OnCurrencySelected(currency))
        },
        supportingContent = {
            Text(
                text = currency.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        },
        leadingContent = {
            if (flagResource == Res.drawable.flag_placeholder) {
                Icon(
                    painter = painterResource(flagResource),
                    contentDescription = "${currency.name} flag",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Image(
                    painter = painterResource(flagResource),
                    contentDescription = "${currency.name} flag",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background, // Items sit on background
            headlineColor = MaterialTheme.colorScheme.onSurface,
            supportingColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme() {
        CurrencyPickerScreen(
            state = CurrencyPickerState(
                currencies = listOf(
                    Currency(
                        code = "USD",
                        name = "United States Dollar",
                        symbol = "$",
                        symbolNative = "$",
                        countryCode = "US"
                    ),
                    Currency(
                        code = "AMD",
                        name = "United States Dollar",
                        symbol = "$",
                        symbolNative = "$",
                        countryCode = "US"
                    )
                ),
                recentCurrencies = listOf(
                    Currency(
                        code = "USD",
                        name = "United States Dollar",
                        symbol = "$",
                        symbolNative = "$",
                        countryCode = "US"
                    ),
                    Currency(
                        code = "AMD",
                        name = "United States Dollar",
                        symbol = "$",
                        symbolNative = "$",
                        countryCode = "US"
                    )
                ),
            ),
            onAction = {},
            onDismiss = {},
        )
    }
}