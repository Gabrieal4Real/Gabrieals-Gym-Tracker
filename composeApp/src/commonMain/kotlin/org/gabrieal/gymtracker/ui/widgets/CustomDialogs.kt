package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialog
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.cancel
import io.github.alexzhirkevich.cupertino.default


@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun CustomAlertDialog(
    titleMessage: Pair<String, String>,
    positiveButton: Pair<String, () -> Unit>,
    negativeButton: Pair<String, () -> Unit>
) {
    AdaptiveAlertDialog(
        onDismissRequest = {
            negativeButton.second.invoke()
        },
        title = {
            Text(titleMessage.first)
        },
        message = {
            Text(titleMessage.second)
        }
    ) {
        cancel(onClick = {
            negativeButton.second.invoke()
        }) {
            Text(negativeButton.first)
        }
        default(onClick = {
            positiveButton.second.invoke()
        }) {
            Text(positiveButton.first)
        }
    }
}