package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gabrieal.gymtracker.util.BoldText
import org.gabrieal.gymtracker.util.Colors

@Composable
fun TitleText(text: String) {
    Text(
        text = text.uppercase(),
        style = TextStyle(fontFamily = BoldText(), letterSpacing = 4.sp, lineHeight = 24.sp),
        color = Colors.TextPrimary,
        fontSize = 18.sp,
        modifier = Modifier.padding(16.dp)
    )
}