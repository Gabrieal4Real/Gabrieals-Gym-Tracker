package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialog
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.cancel
import io.github.alexzhirkevich.cupertino.default
import org.gabrieal.gymtracker.ui.isIOS
import org.gabrieal.gymtracker.util.Colors


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
            if (isIOS())
                Text(titleMessage.first, fontSize = 16.sp)
            else {
                Text(titleMessage.first, fontSize = 22.sp)
            }
        },
        message = {
            if (isIOS())
                Text(titleMessage.second, fontSize = 14.sp, lineHeight = 18.sp)
            else {
                Text(titleMessage.second, fontSize = 16.sp, lineHeight = 20.sp)
            }
        }
    ) {
        cancel(onClick = {
            negativeButton.second.invoke()
        }) {
            if (isIOS()) {
                Text(negativeButton.first, color = Colors.LinkBlue)
            } else {
                Text(negativeButton.first, color = Colors.Black, fontWeight = FontWeight.Medium)
            }
        }
        default(onClick = {
            positiveButton.second.invoke()
        }) {
            if (isIOS()) {
                Text(positiveButton.first, color = Colors.LinkBlue, fontWeight = FontWeight.SemiBold)
            } else {
                Text(positiveButton.first, color = Colors.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}