package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.util.Colors


@Composable
fun ConfirmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Colors.SlightlyDarkerLinkBlue
        ),
        border = BorderStroke(1.dp, Colors.LinkBlue),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        SubtitleText(text.uppercase(), color = Colors.White, modifier = Modifier.padding(4.dp))
    }
}