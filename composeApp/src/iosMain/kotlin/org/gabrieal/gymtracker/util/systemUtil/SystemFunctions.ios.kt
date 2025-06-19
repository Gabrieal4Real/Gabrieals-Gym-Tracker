package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import org.gabrieal.gymtracker.util.app.isValidNumber
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSRange
import platform.Foundation.NSTimeZone
import platform.Foundation.NSURL
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.localeWithLocaleIdentifier
import platform.Foundation.systemTimeZone
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.darwin.NSObject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
        textField?.delegate = FilteringTextFieldDelegate(type)

        when (type) {
            KeyboardType.Number, KeyboardType.Decimal -> {
                textField?.keyboardType = UIKeyboardTypeNumberPad
            }

            KeyboardType.Text -> {
                textField?.keyboardType = UIKeyboardTypeDefault
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
        style = UIAlertActionStyleCancel
    ) { _ -> onOptionSelected(-1) }

    alertController.addAction(cancelAction)
    val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootVC?.presentViewController(alertController, animated = true, completion = null)
}


class FilteringTextFieldDelegate(
    private val type: KeyboardType
) : NSObject(), UITextFieldDelegateProtocol {

    @ExperimentalForeignApi
    override fun textField(
        textField: UITextField,
        shouldChangeCharactersInRange: CValue<NSRange>,
        replacementString: String
    ): Boolean {
        val currentText = (textField.text ?: "") + replacementString

        return when (type) {
            KeyboardType.Number -> {
                currentText.isEmpty() || currentText.isValidNumber()
            }
            else -> true
        }
    }
}

@OptIn(ExperimentalTime::class)
actual fun formatInstantToDate(
    instant: Instant,
    pattern: String
): String {
    val date = NSDate.dateWithTimeIntervalSince1970(instant.epochSeconds.toDouble())
    val formatter = NSDateFormatter()
    formatter.dateFormat = pattern
    formatter.locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")
    return formatter.stringFromDate(date)
}

@OptIn(ExperimentalTime::class)
actual fun parseDateToInstant(dateString: String, pattern: String): Instant {
    val formatter = NSDateFormatter()
    formatter.dateFormat = pattern
    formatter.locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")
    formatter.timeZone = NSTimeZone.systemTimeZone

    val date = formatter.dateFromString(dateString) ?: error("Invalid date string: $dateString")
    val epochSeconds = date.timeIntervalSince1970
    return Instant.fromEpochMilliseconds((epochSeconds * 1000).toLong())
}


@OptIn(ExperimentalTime::class)
actual fun getMondayOrSameInstant(instant: Instant): Instant {
    val calendar = NSCalendar.currentCalendar()
    val date = NSDate.dateWithTimeIntervalSince1970(instant.epochSeconds.toDouble())
    val weekday = calendar.component(NSCalendarUnitWeekday, date)
    val daysToSubtract = if (weekday.toInt() == 2) 0 else (weekday + 5) % 7
    val mondayDate = calendar.dateByAddingUnit(NSCalendarUnitDay, -daysToSubtract, date, 0u)

    val components = mondayDate?.let {
        calendar.components(NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            it
        )
    }
    val startOfMonday = components?.let { calendar.dateFromComponents(it) }

    return Instant.fromEpochSeconds(startOfMonday?.timeIntervalSince1970?.toLong() ?: 0)
}

actual fun keepScreenOn() {
    UIApplication.sharedApplication.idleTimerDisabled = true
}

actual fun allowScreenSleep() {
    UIApplication.sharedApplication.idleTimerDisabled = false
}