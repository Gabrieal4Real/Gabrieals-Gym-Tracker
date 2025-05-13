package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gabrieal.gymtracker.util.BoldText
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.ExtraBoldText
import org.gabrieal.gymtracker.util.MediumText
import org.gabrieal.gymtracker.util.RegularText
import org.gabrieal.gymtracker.util.SemiBoldText

@Composable
fun TitleText(text: String, color: Color = Colors.TextPrimary) {
    Text(
        text = text.uppercase(),
        style = TextStyle(fontFamily = BoldText(), letterSpacing = 4.sp, lineHeight = 24.sp),
        color = color,
        fontSize = 18.sp,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun BiggerText(text: String, modifier: Modifier = Modifier, color: Color = Colors.TextSecondary) {
    Text(
        text = text.uppercase(),
        style = TextStyle(fontFamily = ExtraBoldText(), letterSpacing = 10.sp, lineHeight = 30.sp),
        color = color,
        fontSize = 24.sp,
        modifier = modifier
    )
}

@Composable
fun BigText(text: String, modifier: Modifier = Modifier, color: Color = Colors.TextSecondary, maxLines: Int = Int.MAX_VALUE) {
    Text(
        text = text,
        style = TextStyle(fontFamily = SemiBoldText(), lineHeight = 28.sp),
        color = color,
        fontSize = 20.sp,
        modifier = modifier,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun SubtitleText(text: String, modifier: Modifier = Modifier, color: Color = Colors.TextSecondary, maxLines: Int = Int.MAX_VALUE) {
    Text(text,
        style = TextStyle(fontFamily = SemiBoldText(), lineHeight = 22.sp),
        color = color,
        fontSize = 16.sp,
        modifier = modifier,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun DescriptionText(text: String, modifier: Modifier = Modifier, color: Color = Colors.TextPrimary) {
    Text(text,
        style = TextStyle(fontFamily = MediumText(), lineHeight = 20.sp),
        color = color,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun DescriptionItalicText(text: String, modifier: Modifier = Modifier, color: Color = Colors.TextPrimary) {
    Text(text,
        style = TextStyle(fontFamily = MediumText(), fontStyle = FontStyle.Italic, lineHeight = 20.sp),
        color = color,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun TinyText(text: String, modifier: Modifier = Modifier, color: Color = Colors.TextPrimary) {
    Text(text,
        style = TextStyle(fontFamily = RegularText(), lineHeight = 18.sp),
        color = color,
        fontSize = 12.sp,
        modifier = modifier
    )
}

@Composable
fun TinyItalicText(text: String, modifier: Modifier = Modifier, color: Color = Colors.TextPrimary) {
    Text(text,
        style = TextStyle(fontFamily = RegularText(), fontStyle = FontStyle.Italic, lineHeight = 18.sp),
        color = color,
        fontSize = 12.sp,
        modifier = modifier
    )
}

@Composable
fun LinkText(text: String, modifier: Modifier = Modifier) {
    Text(text,
        style = TextStyle(fontFamily = RegularText(), lineHeight = 18.sp),
        color = Colors.LinkBlue,
        fontSize = 12.sp,
        modifier = modifier
    )
}