package org.gabrieal.gymtracker.util

import androidx.compose.runtime.Composable

import platform.UIKit.*
import platform.Foundation.*

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS doesn't have a direct equivalent to Android's back button
    // This is a no-op implementation as iOS uses swipe gestures or navigation buttons
    // that are handled by the OS
}

@Composable
actual fun openURL(url: String) {
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
actual fun showAlertDialog(
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

    // Present the dialog
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(alertController, animated = true, completion = null)
}