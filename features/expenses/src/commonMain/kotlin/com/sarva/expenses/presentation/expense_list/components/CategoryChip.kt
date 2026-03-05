package com.sarva.expenses.presentation.expense_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.core.domain.expenses.model.ExpenseCategory
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
    isSelected: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    shape: Shape = CircleShape,
) {
    FilterChip(
        modifier = modifier.height(32.dp),
        selected = isSelected,
        onClick = { if (clickable) onClick() },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        leadingIcon = {
            categoryIcon?.let { icon ->
                val painter = when (icon) {
                    is CategoryIcon.Vector -> rememberVectorPainter(icon.imageVector)
                    is CategoryIcon.Custom -> painterResource(icon.resource)
                }

                Surface(
                    modifier = Modifier
                        .size(26.dp)
                        .offset(x = (-5).dp),
                    shape = CircleShape,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    } else {
                        accentColor.copy(alpha = 0.1f)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = accentColor
                        )
                    }
                }
            }
        },
        shape = shape,
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = Color.Transparent,
            borderWidth = 1.dp,
            selectedBorderWidth = 0.dp
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
            selectedContainerColor = accentColor,
            selectedLabelColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    SarvaTheme {
        val category = ExpenseCategory.FOOD

        CategoryChip(
            label = category.getLabel().asStringC(),
            categoryIcon = category.getIcon(),
            onClick = {},
            clickable = true,
            isSelected = true,
            accentColor = MaterialTheme.colorScheme.primary
        )
    }
}