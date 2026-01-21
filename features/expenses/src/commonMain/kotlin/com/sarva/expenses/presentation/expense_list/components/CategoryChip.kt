package com.sarva.expenses.presentation.expense_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.core.domain.model.ExpenseCategory
import com.sarva.core.presentation.CategoryIcon
import com.sarva.core.presentation.getIcon
import com.sarva.core.presentation.getLabel
import com.sarva.designsystem.theme.SarvaTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoryChip(
    label: String,
    categoryIcon: CategoryIcon? = null,
    onClick: () -> Unit,
    clickable: Boolean = true,
    isSelected: Boolean,
    shape: Shape = CircleShape,
    containerColor: Color,
    selectedContainerColor: Color,
    contentColor: Color,
    selectedContentColor: Color,
    iconContainerColor: Color,
    borderColor: Color
) {
    FilterChip(
        modifier = Modifier.height(32.dp).padding(horizontal = 0.dp),
        selected = isSelected,
        onClick = { if (clickable) onClick() },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingIcon = {
            if (categoryIcon != null) {
                val painter = when (categoryIcon) {
                    is CategoryIcon.Vector -> rememberVectorPainter(categoryIcon.imageVector)
                    is CategoryIcon.Custom -> painterResource(categoryIcon.resource)
                }
                if (iconContainerColor == Color.Transparent) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = if (isSelected) selectedContentColor else contentColor
                        )
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .size(28.dp)
                            .offset(x = (-6).dp),
                        shape = CircleShape,
                        color = iconContainerColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = contentColor
                            )
                        }
                    }
                }
            }
        },
        border = BorderStroke(1.dp, borderColor),
        shape = shape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColor,
            selectedContainerColor = selectedContainerColor,
            selectedLabelColor = selectedContentColor,
            labelColor = contentColor,
        ),
    )
}

@Preview(
    name = "Light",
)
@Preview(
    name = "Dark",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
)
@Composable
private fun Preview() {
    SarvaTheme {
        val containerColor = SarvaTheme.colors.expenseContainer
        val contentColor = SarvaTheme.colors.expenseContent
        val cardContainerColor = SarvaTheme.colors.expenseCardContainer
        val category = ExpenseCategory.FOOD

        CategoryChip(
            label = category.getLabel().asStringC(),
            categoryIcon = category.getIcon(),
            onClick = {},
            clickable = true,
            isSelected = false,
            containerColor = cardContainerColor,
            selectedContainerColor = contentColor,
            contentColor = contentColor,
            selectedContentColor = cardContainerColor,
            iconContainerColor = containerColor,
            borderColor = contentColor
        )
    }
}