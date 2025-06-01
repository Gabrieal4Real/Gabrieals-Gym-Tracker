package org.gabrieal.gymtracker.views.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gabrieal.gymtracker.util.appUtil.BoldText
import org.gabrieal.gymtracker.util.appUtil.ExtraBoldText
import org.gabrieal.gymtracker.util.appUtil.MediumText
import org.gabrieal.gymtracker.util.appUtil.RegularText
import org.gabrieal.gymtracker.util.appUtil.SemiBoldText
import org.gabrieal.gymtracker.views.colors

@Composable
fun TitleText(text: String, color: Color = colors.textPrimary) {
    Text(
        text = text.uppercase(),
        style = TextStyle(fontFamily = BoldText(), letterSpacing = 4.sp, lineHeight = 24.sp),
        color = color,
        fontSize = 18.sp,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun BiggerText(text: String, modifier: Modifier = Modifier, color: Color = colors.textSecondary) {
    Text(
        text = text.uppercase(),
        style = TextStyle(fontFamily = ExtraBoldText(), letterSpacing = 10.sp, lineHeight = 30.sp),
        color = color,
        fontSize = 24.sp,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun BigText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colors.textSecondary,
    maxLines: Int = Int.MAX_VALUE
) {
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
fun SubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colors.textSecondary,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text,
        style = TextStyle(fontFamily = SemiBoldText(), lineHeight = 22.sp),
        color = color,
        fontSize = 16.sp,
        modifier = modifier,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
fun DescriptionText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text,
        style = TextStyle(fontFamily = MediumText(), lineHeight = 20.sp),
        color = color,
        fontSize = 14.sp,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun DescriptionItalicText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colors.textPrimary
) {
    Text(
        text,
        style = TextStyle(
            fontFamily = MediumText(),
            fontStyle = FontStyle.Italic,
            lineHeight = 20.sp
        ),
        color = color,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun TinyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text,
        style = TextStyle(fontFamily = RegularText(), lineHeight = 18.sp),
        color = color,
        fontSize = 12.sp,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun TinyItalicText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text,
        style = TextStyle(
            fontFamily = RegularText(),
            fontStyle = FontStyle.Italic,
            lineHeight = 18.sp,
        ),
        color = color,
        fontSize = 12.sp,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun LinkText(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = TextStyle(fontFamily = RegularText(), lineHeight = 18.sp),
        color = colors.linkBlue,
        fontSize = 12.sp,
        modifier = modifier
    )
}