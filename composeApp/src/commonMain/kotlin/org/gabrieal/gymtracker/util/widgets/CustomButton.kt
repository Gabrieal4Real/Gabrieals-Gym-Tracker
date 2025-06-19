package org.gabrieal.gymtracker.util.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.colors


enum class ButtonType(
    val containerColor: Color = colors.slightlyDarkerLinkBlue,
    val disabledContentColor: Color = colors.cardBackground,
    val borderColor: Color = colors.linkBlue,
    val textColor: Color = colors.white,
) {
    BLUE(
        containerColor = colors.slightlyDarkerLinkBlue,
        disabledContentColor = colors.cardBackground,
        borderColor = colors.linkBlue,
        textColor = colors.white
    ),
    RED(
        containerColor = colors.maroon,
        disabledContentColor = colors.cardBackground,
        borderColor = colors.lightMaroon,
        textColor = colors.white
    ),
    OUTLINE(
        containerColor = colors.background,
        disabledContentColor = colors.background,
        borderColor = colors.linkBlue,
        textColor = colors.white
    )
}

@Composable
fun ConfirmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonType: ButtonType = ButtonType.BLUE
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonType.containerColor,
            disabledContentColor = buttonType.disabledContentColor,
        ),
        border = BorderStroke(1.dp, buttonType.borderColor),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
        enabled = enabled
    ) {
        SubtitleText(
            text.uppercase(),
            color = buttonType.textColor,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun TinyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonType: ButtonType = ButtonType.BLUE
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonType.containerColor,
            disabledContentColor = buttonType.disabledContentColor,
        ),
        border = BorderStroke(1.dp, buttonType.borderColor),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
        enabled = enabled
    ) {
        TinyText(
            text.uppercase(),
            color = buttonType.textColor,
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun IncrementDecrementButton(initialValue: Int, minValue: Int, maxValue: Int): Int {
    var selectedValue by remember { mutableStateOf(initialValue) }
    Row {
        val minusBackgroundColor =
            if (selectedValue > minValue) colors.slightlyDarkerLinkBlue else colors.background
        val plusBackgroundColor =
            if (selectedValue < maxValue) colors.slightlyDarkerLinkBlue else colors.background
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(minusBackgroundColor)
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
        DescriptionText(
            selectedValue.toString(),
            modifier = Modifier.align(Alignment.CenterVertically).width(20.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(plusBackgroundColor)
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

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.white,
            checkedTrackColor = colors.bottomNavIndicator,
            uncheckedThumbColor = colors.white.copy(alpha = 0.5f),
            uncheckedTrackColor = colors.slightlyDarkerLinkBlue.copy(alpha = 0.3f),
            disabledUncheckedBorderColor = colors.white.copy(alpha = 0.2f),
            disabledUncheckedThumbColor = colors.white.copy(alpha = 0.5f),
            disabledUncheckedTrackColor = colors.deleteRed.copy(alpha = 0.3f),
        ),
        modifier = modifier,
        thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
    )
}