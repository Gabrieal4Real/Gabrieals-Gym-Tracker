package org.gabrieal.gymtracker.model

enum class FitnessGoal {
    MAINTENANCE, FAT_LOSS, MUSCLE_GAIN
}

data class ProteinInput(
    val weightKg: Double,
    val goal: FitnessGoal,
    val activityLevel: ActivityLevel
)