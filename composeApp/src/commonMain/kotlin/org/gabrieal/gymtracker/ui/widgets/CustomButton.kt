package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.util.appUtil.Colors


@Composable
fun ConfirmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled : Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Colors.SlightlyDarkerLinkBlue,
            disabledBackgroundColor = Colors.CardBackground
        ),
        border = BorderStroke(1.dp, Colors.LinkBlue),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        enabled = enabled
    ) {
        SubtitleText(text.uppercase(), color = Colors.White, modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun IncrementDecrementButton(initialValue: Int, minValue: Int, maxValue: Int): Int {
    var selectedValue by remember { mutableStateOf(initialValue) }
    Row {
        val minusBackgroundColor = if (selectedValue > minValue) Colors.SlightlyDarkerLinkBlue else Colors.Background
        val plusBackgroundColor = if (selectedValue < maxValue) Colors.SlightlyDarkerLinkBlue else Colors.Background
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(minusBackgroundColor, RoundedCornerShape(24.dp))
                .clickable(enabled = selectedValue > minValue) {
                    if (selectedValue > minValue) {
                        selectedValue--
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            DescriptionText("-", color = Colors.White)
        }
        Spacer(modifier = Modifier.width(16.dp))
        DescriptionText(selectedValue.toString(), modifier = Modifier.align(Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(plusBackgroundColor, RoundedCornerShape(24.dp))
                .clickable(enabled = selectedValue < maxValue) {
                    if (selectedValue < maxValue) {
                        selectedValue++
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            DescriptionText("+", color = Colors.White)
        }
    }

    return selectedValue
}