package org.gabrieal.gymtracker.model

import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal

data class ProteinInput(
    val weightKg: Int,
    val goal: FitnessGoal,
    val activityLevel: ActivityLevel
)