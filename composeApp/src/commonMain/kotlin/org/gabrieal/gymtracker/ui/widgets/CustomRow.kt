package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.gabrieal.gymtracker.util.Colors

@Composable
fun BackButtonRow(text: String) {
    val navigator = LocalNavigator.currentOrThrow

    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navigator.pop() }) {
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

@Composable
fun RepRangePicker(
    ranges: List<Pair<Int, Int>>,
    selectedRange: Pair<Int, Int>,
    onRangeSelected: (Pair<Int, Int>) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ranges.forEach { range ->
            val isSelected = range == selectedRange
            val backgroundColor = if (isSelected) Colors.SlightlyDarkerLinkBlue else Colors.Background
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(backgroundColor, RoundedCornerShape(16.dp))
                    .clickable { onRangeSelected(range) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                TinyText("${range.first} - ${range.second} reps")
            }
        }
    }
}
