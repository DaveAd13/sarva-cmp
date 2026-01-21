package com.sarva.expenses.presentation.expense_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.core.domain.model.Expense
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.presentation.CategoryIcon
import com.sarva.core.presentation.getIcon
import com.sarva.core.presentation.util.formatToDisplay
import com.sarva.designsystem.theme.SarvaTheme
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource

@Composable
fun ExpenseCard(
    expense: Expense,
    onClick: () -> Unit,
    cardContainerColor: Color,
    contentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    val painter = when (val iconData = expense.category.getIcon()) {
        is CategoryIcon.Vector -> rememberVectorPainter(iconData.imageVector)
        is CategoryIcon.Custom -> painterResource(iconData.resource)
    }
    Card(
        onClick = onClick,
        modifier = modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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
                color = containerColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = contentColor
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor,
                    maxLines = 1
                )
                Text(
                    text = expense.dateTime.formatToDisplay(),
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.6f),
                    overflow = Ellipsis,
                    maxLines = 1
                )
            }

            Text(
                text = "${expense.amount} ${expense.currency}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = contentColor
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    SarvaTheme(darkTheme = false) {
        val containerColor = SarvaTheme.colors.expenseContainer
        val contentColor = SarvaTheme.colors.expenseContent
        val cardContainerColor = SarvaTheme.colors.expenseCardContainer
        val expense = Expense(
            title = "Trip to Dilijan",
            amount = 370.0,
            currency = "USD",
            category = ExpenseCategory.TRAVEL,
            dateTime = LocalDateTime.parse("2024-09-10"),
        )

        ExpenseCard(
            expense = expense,
            onClick = {},
            cardContainerColor = cardContainerColor,
            contentColor = contentColor,
            containerColor = containerColor
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    SarvaTheme(darkTheme = true) {
        val containerColor = SarvaTheme.colors.expenseContainer
        val contentColor = SarvaTheme.colors.expenseContent
        val cardContainerColor = SarvaTheme.colors.expenseCardContainer
        val expense = Expense(
            title = "Trip to Dilijan",
            amount = 370.0,
            currency = "USD",
            category = ExpenseCategory.TRAVEL,
            dateTime = LocalDateTime.parse("2024-09-10"),
        )

        ExpenseCard(
            expense = expense,
            onClick = {},
            cardContainerColor = cardContainerColor,
            contentColor = contentColor,
            containerColor = containerColor
        )
    }
}

