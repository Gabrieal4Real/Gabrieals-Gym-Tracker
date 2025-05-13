package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.gabrieal.gymtracker.util.appUtil.Colors

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
        TitleText(text)
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
            val backgroundColor =
                if (isSelected) Colors.SlightlyDarkerLinkBlue else Colors.Background
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

@Composable
fun DropDownFilter(
    filterOptions: List<String>,
    selectedFilters: Set<String>,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(modifier.fillMaxHeight()) {
        Icon(
            imageVector = Icons.Rounded.FilterAlt,
            contentDescription = "Filter",
            modifier = Modifier
                .size(32.dp)
                .clickable { isExpanded = !isExpanded },
            tint = Colors.TextPrimary
        )

        if (isExpanded) {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = { isExpanded = false },
            ) {
                Box(
                    modifier = Modifier
                        .heightIn(max = 300.dp)
                        .verticalColumnScrollbar(scrollState)
                        .verticalScroll(scrollState)
                        .background(Colors.Background, RoundedCornerShape(12.dp))
                        .padding(start = 8.dp, top = 8.dp, end = 24.dp, bottom = 8.dp),
                ) {
                    Column {
                        filterOptions.forEach { option ->
                            val isSelected = option in selectedFilters
                            val backgroundColor =
                                if (isSelected) Colors.SlightlyDarkerLinkBlue else Colors.Background
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(backgroundColor, RoundedCornerShape(16.dp))
                                    .clickable { onFilterSelected(option) }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TinyText(option)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Modifier.verticalColumnScrollbar(
    scrollState: ScrollState,
    width: Dp = 4.dp,
    showScrollBarTrack: Boolean = true,
    scrollBarTrackColor: Color = Colors.Background,
    scrollBarColor: Color = Colors.BorderStroke,
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

