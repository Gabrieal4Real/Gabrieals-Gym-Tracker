package org.gabrieal.gymtracker

import androidx.compose.ui.window.ComposeUIViewController
import org.gabrieal.gymtracker.data.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) { App() }