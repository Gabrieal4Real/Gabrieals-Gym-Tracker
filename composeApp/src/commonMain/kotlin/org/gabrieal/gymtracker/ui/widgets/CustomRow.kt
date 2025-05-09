package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.Navigator

@Composable
fun BackButtonRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { Navigator.navigateBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Colors.TextPrimary
            )
        }
        TitleText(text = text)
    }
    Box(modifier = Modifier.background(Colors.BorderStroke).fillMaxWidth().height(1.dp))
}