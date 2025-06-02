package org.gabrieal.gymtracker.util.systemUtil

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
                        val decimalRegex = Regex("^\\d*\\.?\\d*\$")
                        val numberRegex = Regex("^\\d*\$")

                        when (type) {
                            KeyboardType.Decimal -> {
                                if (it.isEmpty() || it.matches(decimalRegex)) {
                                    text = it
                                }
                            }

                            KeyboardType.Number -> {
                                if (it.isEmpty() || it.matches(numberRegex)) {
                                    text = it
                                }
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