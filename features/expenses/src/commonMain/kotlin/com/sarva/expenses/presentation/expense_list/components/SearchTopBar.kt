package com.sarva.expenses.presentation.expense_list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.sarva.core.presentation.textfields.ClearTextIcon
import com.sarva.designsystem.theme.SarvaTheme
import com.sarva.features.expenses.generated.resources.Res
import com.sarva.features.expenses.generated.resources.search_expenses
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    state: TextFieldState,
    onCancelSearch: () -> Unit
) {
    val containerColor = SarvaTheme.colors.expenseContainer
    val contentColor = SarvaTheme.colors.expenseContent
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

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onCancelSearch) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            TextField(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.search_expenses),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = contentColor.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                trailingIcon = {
                    ClearTextIcon(
                        color = contentColor
                    ) {
                        if (state.text.isNotEmpty()) {
                            state.clearText()
                        } else {
                            onCancelSearch()
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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val backState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)

    NavigationBackHandler(
        state = backState,
        isBackEnabled = true,
        onBackCompleted = {
            onCancelSearch()
        }
    )
}