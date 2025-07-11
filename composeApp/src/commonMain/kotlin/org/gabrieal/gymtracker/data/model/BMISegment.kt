package org.gabrieal.gymtracker.data.model

import androidx.compose.ui.graphics.Color

data class BMISegment(
    val range: Float,
    val color: Color,
    val label: String,
    val maxValue: Float
)
