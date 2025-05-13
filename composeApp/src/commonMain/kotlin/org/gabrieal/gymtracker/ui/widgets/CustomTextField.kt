package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.appUtil.MediumText


@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, placeholderText: String, resource: Pair<ImageVector, () -> Unit>? = null) {
    val customSelectionColors = TextSelectionColors(
        handleColor = Colors.SlightlyDarkerLinkBlue,
        backgroundColor = Colors.LinkBlue
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
        Box {
            OutlinedTextField(
                value = value,
                placeholder = { TinyItalicText(placeholderText, color = Colors.PlaceholderColor, modifier = Modifier.align(Alignment.CenterStart)) },
                onValueChange = onValueChange,
                textStyle = TextStyle(fontFamily = MediumText(), lineHeight = 20.sp, color = Colors.Black),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Colors.TextPrimary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Colors.SlightlyDarkerLinkBlue,
                    unfocusedBorderColor = Colors.BorderStroke,
                    cursorColor = Colors.SlightlyDarkerLinkBlue,
                    textColor = Colors.Black,
                    placeholderColor = Colors.PlaceholderColor
                ),
            )
            if (resource != null) {
                Icon(
                    imageVector = resource.first,
                    contentDescription = resource.first.name,
                    tint = Colors.Black,
                    modifier = Modifier.size(48.dp).align(Alignment.CenterEnd).padding(start = 8.dp, end = 8.dp).clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = resource.second
                    )
                )
            }
        }
    }
}