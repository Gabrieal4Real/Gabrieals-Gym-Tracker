package org.gabrieal.gymtracker.util.systemUtil


expect fun requestNotificationPermission()

expect fun notifyPlatform(message: String)

//ConfirmButton("Request Permission", onClick = {
//    requestNotificationPermission()
//})
//ConfirmButton("Test Notification", onClick = {
//    notifyPlatform("Notification")
//})