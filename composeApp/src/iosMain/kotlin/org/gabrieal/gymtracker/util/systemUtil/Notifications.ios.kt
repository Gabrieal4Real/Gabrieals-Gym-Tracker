package org.gabrieal.gymtracker.util.systemUtil

import platform.Foundation.NSUUID
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

actual fun requestNotificationPermission() {
    val center = UNUserNotificationCenter.currentNotificationCenter()
    center.requestAuthorizationWithOptions(
        options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
        completionHandler = { _, error ->
            if (error != null) {
                println("iOS Notification permission error: ${'$'}{error.localizedDescription}")
            } else {
                println("iOS Notification permission granted: ${'$'}granted")
            }
        }
    )
}

actual fun notifyPlatform(message: String) {
    val content = UNMutableNotificationContent().apply {
        setTitle("Gym Tracker")
        setBody(message)
        setSound(UNNotificationSound.defaultSound())
    }

    val request = UNNotificationRequest.requestWithIdentifier(
        identifier = NSUUID().UUIDString,
        content = content,
        trigger = null
    )

    UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, withCompletionHandler = null)
}