package org.gabrieal.gymtracker.data.model

import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.Gender

data class CalorieInput(
    val gender: Gender,
    val age: Int,
    val weightKg: Double,
    val heightCm: Double,
    val activityLevel: ActivityLevel
)

data class CalorieGoalResult(
    val label: String,
    val weightChangePerWeekKg: Double,
    val calories: Int,
    val percentageOfMaintenance: Int
)