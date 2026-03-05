package com.sarva.expenses.presentation.expense_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.core.domain.expenses.model.Expense
import com.sarva.core.domain.expenses.model.ExpenseCategory
import com.sarva.core.presentation.CategoryIcon
import com.sarva.core.presentation.formatting.formatCurrency
import com.sarva.core.presentation.formatting.formatToShortDisplay
import com.sarva.core.presentation.getIcon
import com.sarva.designsystem.theme.SarvaTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource

@Composable
fun ExpenseCard(
    expense: Expense,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = SarvaTheme.colors.expenses
    val painter = when (val iconData = expense.category.getIcon()) {
        is CategoryIcon.Vector -> rememberVectorPainter(iconData.imageVector)
        is CategoryIcon.Custom -> painterResource(iconData.resource)
    }

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = modifier.padding(bottom = 12.dp),
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        },
        onDismiss = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                scope.launch {
                    swipeToDismissBoxState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            }
        },
        gesturesEnabled = true,
        enableDismissFromStartToEnd = false,
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.2f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = expense.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = expense.dateTime.formatToShortDisplay(),
                        style = MaterialTheme.typography.bodySmall,
                        color =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        overflow = Ellipsis,
                        maxLines = 1
                    )
                }

                Text(
                    text = formatCurrency(expense.amount, expense.currency),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color =  MaterialTheme.colorScheme.onSurface,
                    overflow = Ellipsis,
                    maxLines = 1
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
        val expense = Expense(
            title = "Trip to Dilijan",
            amount = 370.0,
            currency = "USD",
            category = ExpenseCategory.TRAVEL,
            dateTime = LocalDateTime.parse("2024-09-10T00:00:00"),
        )

        ExpenseCard(
            expense = expense,
            onClick = {},
            onDelete = {},
        )
    }
}
