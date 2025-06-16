package org.gabrieal.gymtracker.util.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.util.app.formatRestTime
import org.gabrieal.gymtracker.util.navigation.AppNavigator

@Composable
fun BackButtonRow(text: String, backButtonAction: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            backButtonAction?.invoke() ?: AppNavigator.navigateBack()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                contentDescription = "Back",
                tint = colors.textPrimary
            )
        }
        TitleText(text)
    }

    CustomHorizontalDivider()
}

@Composable
fun TitleRow(text: String) {
    TitleText(text)
    CustomHorizontalDivider()
}

@Composable
fun CustomHorizontalDivider(ratio: Float = 1f) {
    HorizontalDivider(
        color = colors.borderStroke,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(ratio)
    )
}

@Composable
fun DashedDivider() {
    Canvas(
        modifier = Modifier
            .height(36.dp)
            .width(1.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
        drawLine(
            color = Color.Gray,
            start = Offset.Zero,
            end = Offset(0f, size.height),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
    }
}

@Composable
fun CustomVerticalDivider() {
    VerticalDivider(
        color = colors.borderStroke,
        thickness = 1.dp,
        modifier = Modifier.fillMaxHeight()
    )
}

@Composable
fun RepRangePicker(
    ranges: List<Pair<Int, Int>>,
    selectedRange: Pair<Int, Int>,
    onRangeSelected: (Pair<Int, Int>) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(ranges.size) {
            val range = ranges[it]
            FilterChip(
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = colors.background,
                    selectedContainerColor = colors.slightlyDarkerLinkBlue,
                    selectedTrailingIconColor = colors.white
                ),
                shape = RoundedCornerShape(16.dp),
                selected = range == selectedRange,
                onClick = { onRangeSelected(range) },
                elevation = null,
                label = {
                    TinyText(
                        "${range.first} - ${range.second} reps",
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 12.dp),
                        color = colors.white
                    )
                },
            )
        }
    }
}

@Composable
fun Modifier.verticalColumnScrollbar(
    scrollState: ScrollState,
    width: Dp = 4.dp,
    showScrollBarTrack: Boolean = true,
    scrollBarTrackColor: Color = colors.background,
    scrollBarColor: Color = colors.borderStroke,
    scrollBarCornerRadius: Float = 16f,
    endPadding: Float = 12f
): Modifier {
    return drawWithContent {
        drawContent()
        val viewportHeight = this.size.height
        val totalContentHeight = scrollState.maxValue.toFloat() + viewportHeight
        val scrollValue = scrollState.value.toFloat()
        val scrollBarHeight =
            (viewportHeight / totalContentHeight) * viewportHeight
        val scrollBarStartOffset =
            (scrollValue / totalContentHeight) * viewportHeight
        if (showScrollBarTrack) {
            drawRoundRect(
                cornerRadius = CornerRadius(scrollBarCornerRadius),
                color = scrollBarTrackColor,
                topLeft = Offset(this.size.width - endPadding, 0f),
                size = Size(width.toPx(), viewportHeight),
            )
        }
        drawRoundRect(
            cornerRadius = CornerRadius(scrollBarCornerRadius),
            color = scrollBarColor,
            topLeft = Offset(this.size.width - endPadding, scrollBarStartOffset),
            size = Size(width.toPx(), scrollBarHeight)
        )
    }
}

@Composable
fun DropdownMenuBox(
    value: String,
    placeholderText: String,
    onSelected: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        CustomNonClickableTextField(
            value = value,
            onClick = {
                expanded = !expanded
            },
            placeholderText = placeholderText,
        )
        Spacer(modifier = Modifier.height(2.dp))
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.8f),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = colors.textPrimary,
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected.invoke(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ClickToStartTimerBar(
    isRunning: Boolean,
    currentTime: Int,
    totalTime: Int,
    onClick: () -> Unit,
    onRestart: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (totalTime > 0) currentTime / totalTime.toFloat() else 1f,
        animationSpec = tween(500),
        label = "progress"
    )

    val shape = RoundedCornerShape(12.dp)
    val backgroundColor = colors.placeholderColor
    val progressColor = colors.slightlyDarkerLinkBlue

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(42.dp)
                .background(progressColor)
        )

        Icon(
            imageVector = if (isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            contentDescription = "Pause",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(32.dp)
                .padding(start = 4.dp)
                .clickable { onClick() }
        )

        TinyText(
            text = if (currentTime <= 0) "Timer" else formatRestTime(currentTime),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(4.dp),
            color = Color.White
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.RestartAlt,
                contentDescription = "Restart",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 4.dp)
                    .clickable { onRestart() }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Rounded.Clear,
                contentDescription = "Reset",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 4.dp)
                    .clickable { onReset() }
            )
        }
    }
}

@Composable
fun CustomGrabber(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(top = 16.dp)
            .width(48.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(colors.placeholderColor)
    )
}




