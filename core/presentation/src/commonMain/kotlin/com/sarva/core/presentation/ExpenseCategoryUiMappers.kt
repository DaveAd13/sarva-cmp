package com.sarva.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.LocalTaxi
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector
import com.sarva.core.domain.model.expense.ExpenseCategory
import com.sarva.core.presentation.generated.resources.Res
import com.sarva.core.presentation.generated.resources.bills
import com.sarva.core.presentation.generated.resources.education
import com.sarva.core.presentation.generated.resources.entertainment
import com.sarva.core.presentation.generated.resources.food
import com.sarva.core.presentation.generated.resources.groceries
import com.sarva.core.presentation.generated.resources.health
import com.sarva.core.presentation.generated.resources.ic_travel
import com.sarva.core.presentation.generated.resources.other
import com.sarva.core.presentation.generated.resources.shopping
import com.sarva.core.presentation.generated.resources.sports
import com.sarva.core.presentation.generated.resources.subscriptions
import com.sarva.core.presentation.generated.resources.transport
import com.sarva.core.presentation.generated.resources.travel
import com.sarva.core.presentation.util.UiText
import org.jetbrains.compose.resources.DrawableResource

fun ExpenseCategory.getLabel(): UiText = when (this) {
    ExpenseCategory.FOOD -> UiText.StringRes(Res.string.food)
    ExpenseCategory.SHOPPING -> UiText.StringRes(Res.string.shopping)
    ExpenseCategory.TRANSPORT -> UiText.StringRes(Res.string.transport)
    ExpenseCategory.GROCERIES -> UiText.StringRes(Res.string.groceries)
    ExpenseCategory.BILLS -> UiText.StringRes(Res.string.bills)
    ExpenseCategory.SUBSCRIPTIONS -> UiText.StringRes(Res.string.subscriptions)
    ExpenseCategory.HEALTH -> UiText.StringRes(Res.string.health)
    ExpenseCategory.ENTERTAINMENT -> UiText.StringRes(Res.string.entertainment)
    ExpenseCategory.TRAVEL -> UiText.StringRes(Res.string.travel)
    ExpenseCategory.SPORTS -> UiText.StringRes(Res.string.sports)
    ExpenseCategory.EDUCATION -> UiText.StringRes(Res.string.education)
    ExpenseCategory.OTHER -> UiText.StringRes(Res.string.other)
}

fun ExpenseCategory.getIcon(): CategoryIcon = when (this) {
    ExpenseCategory.FOOD -> CategoryIcon.Vector(Icons.Rounded.Fastfood)
    ExpenseCategory.SHOPPING -> CategoryIcon.Vector(Icons.Rounded.ShoppingCart)
    ExpenseCategory.TRANSPORT -> CategoryIcon.Vector(Icons.Rounded.LocalTaxi)
    ExpenseCategory.GROCERIES -> CategoryIcon.Vector(Icons.Rounded.Store)
    ExpenseCategory.BILLS -> CategoryIcon.Vector(Icons.Rounded.AttachMoney)
    ExpenseCategory.SUBSCRIPTIONS -> CategoryIcon.Vector(Icons.Rounded.Subscriptions)
    ExpenseCategory.HEALTH -> CategoryIcon.Vector(Icons.Rounded.HealthAndSafety)
    ExpenseCategory.ENTERTAINMENT -> CategoryIcon.Vector(Icons.Rounded.Celebration)
    ExpenseCategory.TRAVEL -> CategoryIcon.Custom(Res.drawable.ic_travel)
    ExpenseCategory.SPORTS -> CategoryIcon.Vector(Icons.Rounded.FitnessCenter)
    ExpenseCategory.EDUCATION -> CategoryIcon.Vector(Icons.Rounded.School)
    ExpenseCategory.OTHER -> CategoryIcon.Vector(Icons.AutoMirrored.Rounded.ReceiptLong)
}

sealed class CategoryIcon {
    data class Vector(val imageVector: ImageVector) : CategoryIcon()
    data class Custom(val resource: DrawableResource) : CategoryIcon()
}