package org.gabrieal.gymtracker.features.calculator.viewmodel

import org.gabrieal.gymtracker.model.Profile
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal

data class CalculatorUiState(
    val profile: Profile? = null,
    val weight: Int? = null,
    val goal: FitnessGoal? = null,
    val activityLevel: ActivityLevel? = null
)