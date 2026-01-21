package com.sarva.app.features.home.presentation

sealed interface HomeNavigationAction {
    data object OpenNotes : HomeNavigationAction
    data object OpenFitness : HomeNavigationAction
    data object OpenTasks : HomeNavigationAction
    data object OpenExpenses : HomeNavigationAction
    data object OpenCalendar : HomeNavigationAction
    data object OpenPlaces : HomeNavigationAction
}