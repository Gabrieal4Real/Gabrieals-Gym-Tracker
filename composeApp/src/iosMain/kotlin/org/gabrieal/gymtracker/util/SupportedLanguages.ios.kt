package org.gabrieal.gymtracker.util

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual val language: String
    get() = NSLocale.currentLocale.languageCode