package org.gabrieal.gymtracker.model

enum class Gender {
    MALE, FEMALE
}

enum class ActivityLevel(val multiplier: Double) {
    SEDENTARY(1.2),
    LIGHTLY_ACTIVE(1.375),
    MODERATELY_ACTIVE(1.55),
    VERY_ACTIVE(1.725),
    SUPER_ACTIVE(1.9),
}

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