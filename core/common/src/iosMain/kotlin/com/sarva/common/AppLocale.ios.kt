package com.sarva.common

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getCurrentLocale(): String {
    return NSLocale.currentLocale.languageCode
}