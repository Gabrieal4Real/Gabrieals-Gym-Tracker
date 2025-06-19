package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import gymtracker.composeapp.generated.resources.Res.readBytes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalResourceApi::class)
@Composable
fun readFile(path: String): String {
    var bytes by remember { mutableStateOf(ByteArray(0)) }

    LaunchedEffect(Unit) {
        try {
            bytes = readBytes("files/$path")
        } catch (e: Exception) {
            println("readFileError: ${e.message}")
        }
    }

    return bytes.decodeToString()
}

@Composable
expect fun OpenURL(url: String)

@Composable
expect fun ShowAlertDialog(
    titleMessage: Pair<String, String>,
    positiveButton: Pair<String, () -> Unit>,
    negativeButton: Pair<String, () -> Unit>
)

@Composable
expect fun ShowInputDialog(
    titleMessage: Pair<String, String>,
    positiveButton: Pair<String, (String) -> Unit>,
    negativeButton: Pair<String, () -> Unit>,
    type: KeyboardType
)

@Composable
expect fun ShowSpinner(
    title: String,
    options: List<String>,
    onOptionSelected: (Int) -> Unit
)

@Composable
expect fun ShowToast(message: String)

expect fun getTodayDayName(): String

@OptIn(ExperimentalTime::class)
expect fun formatInstantToDate(instant: Instant, pattern: String): String

@OptIn(ExperimentalTime::class)
expect fun parseDateToInstant(dateString: String, pattern: String): Instant

@OptIn(ExperimentalTime::class)
expect fun getMondayOrSameInstant(instant: Instant): Instant

expect fun keepScreenOn()

expect fun allowScreenSleep()
