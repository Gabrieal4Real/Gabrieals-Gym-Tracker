package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.runtime.Composable

import platform.UIKit.*
import platform.Foundation.*

@Composable
actual fun OpenURL(url: String) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
        UIApplication.sharedApplication.openURL(
            nsUrl,
            options = emptyMap<Any?, Any>(),
            completionHandler = { success ->
                println("URL opened successfully: $success")
            }
        )
    }
}

@Composable
actual fun getCurrentContext(): Any? {
    return null
}

@Composable
actual fun ShowAlertDialog(
    titleMessage: Pair<String, String>,
    positiveButton: Pair<String, () -> Unit>,
    negativeButton: Pair<String, () -> Unit>
) {
    val alertController = UIAlertController.alertControllerWithTitle(
        title = titleMessage.first,
        message = titleMessage.second,
        preferredStyle = UIAlertControllerStyleAlert
    )

    val positiveAction = UIAlertAction.actionWithTitle(
        title = positiveButton.first,
        style = UIAlertActionStyleDefault
    ) {
        positiveButton.second.invoke()
    }

    alertController.addAction(positiveAction)

    val negativeAction = UIAlertAction.actionWithTitle(
        title = negativeButton.first,
        style = UIAlertActionStyleCancel
    ) {
        negativeButton.second.invoke()
    }
    alertController.addAction(negativeAction)

    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(alertController, animated = true, completion = null)
}

@Composable
actual fun ShowToast(message: String) {
    val alertController = UIAlertController.alertControllerWithTitle(
        title = "Alert",
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )

    val positiveAction = UIAlertAction.actionWithTitle(
        title = "OK",
        style = UIAlertActionStyleDefault,
        handler = null
    )

    alertController.addAction(positiveAction)

    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(alertController, animated = true, completion = null)
}

actual fun getTodayDayName(): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = "EEEE"
    val date = NSDate()
    return dateFormatter.stringFromDate(date)
}

@Composable
actual fun ShowInputDialog(
    titleMessage: Pair<String, String>,
    positiveButton: Pair<String, (String) -> Unit>,
    negativeButton: Pair<String, () -> Unit>
) {
    val alertController = UIAlertController.alertControllerWithTitle(
        title = titleMessage.first,
        message = titleMessage.second,
        preferredStyle = UIAlertControllerStyleAlert
    )

    alertController.addTextFieldWithConfigurationHandler(null)

    val positiveAction = UIAlertAction.actionWithTitle(
        title = positiveButton.first,
        style = UIAlertActionStyleDefault
    ) { _ ->
        val textField = alertController.textFields?.firstOrNull() as? UITextField
        positiveButton.second(textField?.text ?: "")
    }
    alertController.addAction(positiveAction)

    // Negative action
    val negativeAction = UIAlertAction.actionWithTitle(
        title = negativeButton.first,
        style = UIAlertActionStyleCancel
    ) { _ ->
        negativeButton.second()
    }
    alertController.addAction(negativeAction)

    // Present the alert
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(alertController, animated = true, completion = null)
}