package com.sarva.app.features.home.presentation

sealed interface HomeAction {

    data class OnTaskToggle(val id: String) : HomeAction
    data object OnRefresh : HomeAction
    data object RequestHealthPermission : HomeAction
}