package org.gabrieal.gymtracker.util.enums


enum class ActivityLevel(
    val multiplier: Double,
    val displayName: String = "",
    val description: String = ""
) {
    SEDENTARY(1.2, "Sedentary", "Sedentary: Little or no exercise"),
    LIGHTLY_ACTIVE(1.375, "Lightly Active", "Light: Exercise 1-3 times/week"),
    MODERATELY_ACTIVE(1.55, "Moderately Active", "Moderate: Exercise 4-5 times/week"),
    VERY_ACTIVE(1.725, "Very Active", "Active: Daily exercise or intense exercise 3-4 times/week"),
    SUPER_ACTIVE(1.9, "Super Active", "Super Active: Intense exercise 6-7 times/week"),
}