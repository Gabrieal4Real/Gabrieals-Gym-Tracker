package org.gabrieal.gymtracker.util.systemUtil

import platform.UserNotifications.*
import platform.Foundation.*

actual class NotificationPermissionHandler {
    actual fun requestPermission() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
            completionHandler = { granted, error ->
                if (error != null) {
                    println("iOS Notification permission error: ${'$'}{error.localizedDescription}")
                } else {
                    println("iOS Notification permission granted: ${'$'}granted")
                }
            }
        )
    }
}

actual fun notifyPlatform(message: String) {
    val content = UNMutableNotificationContent().apply {
        setTitle("Gym Tracker")
        setBody(message)
    }

    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, repeats = false)
    val request = UNNotificationRequest.requestWithIdentifier(
        identifier = NSUUID().UUIDString,
        content = content,
        trigger = trigger
    )

    UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, withCompletionHandler = null)
}

