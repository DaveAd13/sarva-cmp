package com.sarva.designsystem

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformSystemUiControls(useDarkIcons: Boolean) {
    // iOS handles status bar style mostly via the Info.plist
    // or the ViewController's preferredStatusBarStyle.
    // If you're using a standard CMP setup, the system often handles this,
    // but you can trigger a refresh here if using a native bridge.
}