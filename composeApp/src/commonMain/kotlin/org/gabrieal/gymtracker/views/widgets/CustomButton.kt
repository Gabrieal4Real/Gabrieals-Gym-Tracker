package org.gabrieal.gymtracker.views.widgets

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.views.colors


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
            containerColor = colors.slightlyDarkerLinkBlue,
            disabledContentColor = colors.cardBackground,
        ),
        border = BorderStroke(1.dp, colors.linkBlue),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
        enabled = enabled
    ) {
        SubtitleText(text.uppercase(), color = colors.white, modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun IncrementDecrementButton(initialValue: Int, minValue: Int, maxValue: Int): Int {
    var selectedValue by remember { mutableStateOf(initialValue) }
    Row {
        val minusBackgroundColor = if (selectedValue > minValue) colors.slightlyDarkerLinkBlue else colors.background
        val plusBackgroundColor = if (selectedValue < maxValue) colors.slightlyDarkerLinkBlue else colors.background
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
            DescriptionText("-", color = colors.white)
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
            DescriptionText("+", color = colors.white)
        }
    }

    return selectedValue
}