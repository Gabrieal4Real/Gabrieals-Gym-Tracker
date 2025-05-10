package org.gabrieal.gymtracker.util

import java.util.Locale

actual val language: String
    get() = Locale.getDefault().language