package com.sarva.app.features.more.presentation

sealed interface MoreAction {
    data object OnThemeClicked : MoreAction
    data object OnCurrencyClicked : MoreAction
    data object OnStepGoalClicked : MoreAction
    data object OnLoginClicked : MoreAction
}