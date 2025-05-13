package org.gabrieal.gymtracker.util.systemUtil

import java.util.Locale

actual val language: String
    get() = Locale.getDefault().language