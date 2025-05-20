package org.gabrieal.gymtracker.util.systemUtil

import android.content.Intent
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
actual fun OpenURL(url: String) {
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        url.toUri()
    )
    LocalContext.current.startActivity(browserIntent)
}

@Composable
actual fun getCurrentContext(): Any? {
    return LocalContext.current
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
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}

actual fun getTodayDayName(): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault()) // Full day name, e.g. Monday
    return sdf.format(calendar.time)
}