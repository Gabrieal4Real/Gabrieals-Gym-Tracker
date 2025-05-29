package org.gabrieal.gymtracker

import androidx.compose.ui.window.ComposeUIViewController
import org.gabrieal.gymtracker.util.systemUtil.NotificationPermissionHandler
import org.gabrieal.gymtracker.views.App

fun MainViewController() = ComposeUIViewController { App(NotificationPermissionHandler()) }