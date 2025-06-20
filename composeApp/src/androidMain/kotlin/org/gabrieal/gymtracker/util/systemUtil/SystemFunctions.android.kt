package org.gabrieal.gymtracker.util.systemUtil

import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import org.gabrieal.gymtracker.util.app.isValidDecimal
import org.gabrieal.gymtracker.util.app.isValidNumber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
actual fun OpenURL(url: String) {
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        url.toUri()
    )

    val context = activityReference ?: return

    context.startActivity(browserIntent)
}

@Composable
actual fun ShowAlertDialog(
    titleMessage: Pair<String, String>,
    positiveButton: Pair<String, () -> Unit>,
    negativeButton: Pair<String, () -> Unit>
) {
    AlertDialog(
        onDismissRequest = {
            negativeButton.second.invoke()
        },
        title = { Text(text = titleMessage.first) },
        text = { Text(text = titleMessage.second) },
        confirmButton = {
            TextButton(onClick = { positiveButton.second.invoke() }) { Text(positiveButton.first) }
        },
        dismissButton = {
            TextButton(onClick = { negativeButton.second.invoke() }) { Text(negativeButton.first) }
        }
    )
}

@Composable
actual fun ShowToast(message: String) {
    Toast.makeText(activityReference, message, Toast.LENGTH_SHORT).show()
}

actual fun getTodayDayName(): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault()) // Full day name, e.g. Monday
    return sdf.format(calendar.time)
}

@Composable
actual fun ShowInputDialog(
    titleMessage: Pair<String, String>,
    positiveButton: Pair<String, (String) -> Unit>,
    negativeButton: Pair<String, () -> Unit>,
    type: KeyboardType
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = negativeButton.second,
        title = { Text(titleMessage.first) },
        text = {
            Column {
                TextField(
                    value = text,
                    onValueChange = {
                        when (type) {
                            KeyboardType.Decimal -> {
                                text =
                                    it.takeIf { input -> input.isEmpty() || input.isValidDecimal() } ?: text
                            }

                            KeyboardType.Number -> {
                                text =
                                    it.takeIf { input -> input.isEmpty() || input.isValidNumber() } ?: text
                            }

                            else -> {
                                text = it
                            }
                        }
                    },
                    placeholder = { Text(titleMessage.second) },
                    keyboardOptions = KeyboardOptions(keyboardType = type)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (text.isEmpty()) {
                    negativeButton.second()
                    return@TextButton
                }
                positiveButton.second(text)
            }) {
                Text(positiveButton.first)
            }
        },
        dismissButton = {
            TextButton(onClick = negativeButton.second) {
                Text(negativeButton.first)
            }
        }
    )
}

@Composable
actual fun ShowSpinner(
    title: String,
    options: List<String>,
    onOptionSelected: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onOptionSelected(-1) },
        title = { Text(title) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxHeight(0.5f)) {
                items(options.size) { index ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = options[index],
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOptionSelected(index)
                                }
                                .padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onOptionSelected(-1)
            }) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalTime::class)
actual fun formatInstantToDate(
    instant: Instant,
    pattern: String
): String {
    val date = Date(instant.toEpochMilliseconds())
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(date)
}

@OptIn(ExperimentalTime::class)
actual fun parseDateToInstant(dateString: String, pattern: String): Instant {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    val date: Date = formatter.parse(dateString) ?: error("Invalid date format")
    return Instant.fromEpochMilliseconds(date.time)
}

@OptIn(ExperimentalTime::class)
actual fun getMondayOrSameInstant(instant: Instant): Instant {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = instant.toEpochMilliseconds()
    }

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysToSubtract = if (dayOfWeek == Calendar.MONDAY) 0 else (dayOfWeek + 5) % 7
    calendar.add(Calendar.DAY_OF_MONTH, -daysToSubtract)

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return Instant.fromEpochMilliseconds(calendar.timeInMillis)
}

actual fun keepScreenOn() {
    activityReference?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

actual fun allowScreenSleep() {
    activityReference?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}