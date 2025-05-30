package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSURL
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIKeyboardTypeDecimalPad
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UITextField

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
    negativeButton: Pair<String, () -> Unit>,
    type: KeyboardType
) {
    val alertController = UIAlertController.alertControllerWithTitle(
        title = titleMessage.first,
        message = titleMessage.second,
        preferredStyle = UIAlertControllerStyleAlert
    )

    alertController.addTextFieldWithConfigurationHandler { textField ->
        when (type) {
            KeyboardType.Number -> {
                textField?.keyboardType = UIKeyboardTypeNumberPad
            }

            KeyboardType.Text -> {
                textField?.keyboardType = UIKeyboardTypeDefault
            }

            KeyboardType.Decimal -> {
                textField?.keyboardType = UIKeyboardTypeDecimalPad
            }
        }
    }

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

@Composable
actual fun ShowSpinner(
    title: String,
    options: List<String>,
    onOptionSelected: (Int) -> Unit
) {
    val alertController = UIAlertController.alertControllerWithTitle(
        title = title,
        message = null,
        preferredStyle = UIAlertControllerStyleActionSheet
    )
    options.forEachIndexed { index, option ->
        val action = UIAlertAction.actionWithTitle(
            title = option,
            style = UIAlertActionStyleDefault
        ) { _ -> onOptionSelected(index) }
        alertController.addAction(action)
    }
    val cancelAction = UIAlertAction.actionWithTitle(
        title = "Cancel",
        style = UIAlertActionStyleCancel,
        handler = null
    )
    alertController.addAction(cancelAction)
    val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootVC?.presentViewController(alertController, animated = true, completion = null)
}