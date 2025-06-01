package org.gabrieal.gymtracker.model.enums

enum class Gender {
    MALE, FEMALE
}

enum class ActivityLevel(val multiplier: Double) {
    SEDENTARY(1.2),
    LIGHTLY_ACTIVE(1.375),
    MODERATELY_ACTIVE(1.55),
    VERY_ACTIVE(1.725),
    SUPER_ACTIVE(1.9)
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

object CalorieCalculator {

    fun calculateBMR(input: CalorieInput): Double {
        return when (input.gender) {
            Gender.MALE -> (10 * input.weightKg) + (6.25 * input.heightCm) - (5 * input.age) + 5
            Gender.FEMALE -> (10 * input.weightKg) + (6.25 * input.heightCm) - (5 * input.age) - 161
        }
    }

    fun calculateMaintenanceCalories(input: CalorieInput): Double {
        val bmr = calculateBMR(input)
        return bmr * input.activityLevel.multiplier
    }

    fun generateGoalBreakdown(input: CalorieInput): List<CalorieGoalResult> {
        val maintenance = calculateMaintenanceCalories(input)

        val goals = listOf(
            Triple("Extreme weight loss", -1.0, -1000),
            Triple("Weight loss", -0.5, -500),
            Triple("Mild weight loss", -0.25, -250),
            Triple("Maintain weight", 0.0, 0),
            Triple("Mild weight gain", 0.25, 250),
            Triple("Weight gain", 0.5, 500),
            Triple("Fast weight gain", 1.0, 1000),
        )

        return goals.map { (label, weightChange, calorieDiff) ->
            val calories = (maintenance + calorieDiff).coerceAtLeast(1000.0).toInt()
            val percentage = ((calories / maintenance) * 100).toInt()
            CalorieGoalResult(label, weightChange, calories, percentage)
        }
    }
}
