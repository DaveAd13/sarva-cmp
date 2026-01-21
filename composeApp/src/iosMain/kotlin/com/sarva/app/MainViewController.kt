package com.sarva.app

import androidx.compose.ui.window.ComposeUIViewController
import com.sarva.app.di.initKoin

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController { App() }

@Suppress("unused")
fun doInitKoin() {
    initKoin()
}