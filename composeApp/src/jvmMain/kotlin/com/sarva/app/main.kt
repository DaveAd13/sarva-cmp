package com.sarva.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sarva.app.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Sarva",
        ) {
            App()
        }
    }
}