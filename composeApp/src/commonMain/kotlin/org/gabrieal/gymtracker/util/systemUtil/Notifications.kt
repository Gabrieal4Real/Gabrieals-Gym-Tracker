package org.gabrieal.gymtracker.util.systemUtil


expect class NotificationPermissionHandler {
    fun requestPermission()
}

expect fun notifyPlatform(message: String)
