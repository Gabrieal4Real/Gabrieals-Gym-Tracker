package org.gabrieal.gymtracker.views.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gabrieal.gymtracker.util.appUtil.MediumText
import org.gabrieal.gymtracker.views.colors


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    resource: Pair<ImageVector, () -> Unit>? = null
) {
    val customSelectionColors = TextSelectionColors(
        handleColor = colors.slightlyDarkerLinkBlue,
        backgroundColor = colors.linkBlue
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
        Box {
            OutlinedTextField(
                value = value,
                placeholder = {
                    TinyItalicText(
                        placeholderText,
                        color = colors.placeholderColor,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                },
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontFamily = MediumText(),
                    lineHeight = 20.sp,
                    color = colors.black
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        colors.textPrimary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colors.slightlyDarkerLinkBlue,
                    unfocusedIndicatorColor = colors.borderStroke,
                    cursorColor = colors.slightlyDarkerLinkBlue,
                    focusedTextColor = colors.black,
                    unfocusedTextColor = colors.black,
                    focusedPlaceholderColor = colors.placeholderColor,
                    unfocusedPlaceholderColor = colors.placeholderColor,
                    focusedLabelColor = colors.slightlyDarkerLinkBlue,
                    unfocusedLabelColor = colors.borderStroke,
                    focusedContainerColor = colors.white,
                    unfocusedContainerColor = colors.white
                ),
                trailingIcon = {
                    if (resource != null) {
                        Icon(
                            imageVector = resource.first,
                            contentDescription = resource.first.name,
                            tint = colors.black,
                            modifier = Modifier.size(48.dp).align(Alignment.CenterEnd)
                                .padding(start = 8.dp, end = 8.dp).clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = resource.second
                            )
                        )
                    }
                }
            )
        }
    }
}