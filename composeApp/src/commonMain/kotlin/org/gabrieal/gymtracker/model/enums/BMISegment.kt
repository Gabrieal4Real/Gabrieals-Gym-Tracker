package org.gabrieal.gymtracker.model.enums

import androidx.compose.ui.graphics.Color

data class BMISegment(
    val range: Float,
    val color: Color,
    val label: String,
    val maxValue: Float
)

val bmiSegments =  listOf(
    BMISegment(3f, Color(0xFFBE3E33), "Underweight (Severely)", 16f),  // 13 - 16
    BMISegment(1f, Color(0xFFD07560), "Underweight (Moderate)", 17f),  // 16 - 17
    BMISegment(1.5f, Color(0xFFF1BC6A), "Underweight (Mild)", 18.5f),  // 17 - 18.5
    BMISegment(7f, Color(0xFF8DC367), "Normal", 25.5f),                // 18.5 - 25.5
    BMISegment(4.5f, Color(0xFFF1BC6A), "Overweight", 30f),            // 25.5 - 30
    BMISegment(5f, Color(0xFFE2AD8D), "Obese Class I", 35f),           // 30 - 35
    BMISegment(5f, Color(0xFFD07560), "Obese Class II", 40f),          // 35 - 40
    BMISegment(1f, Color(0xFFBE3E33), "Obese Class III", 41f)          // 40 - 41
)