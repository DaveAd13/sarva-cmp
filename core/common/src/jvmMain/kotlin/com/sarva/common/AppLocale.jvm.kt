package com.sarva.common

import java.util.Locale

actual fun getCurrentLocale(): String {
    return Locale.getDefault().language
}