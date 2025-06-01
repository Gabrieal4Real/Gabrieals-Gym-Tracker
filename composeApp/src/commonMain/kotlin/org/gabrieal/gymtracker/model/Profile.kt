package org.gabrieal.gymtracker.model

import kotlinx.serialization.Serializable
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal
import org.gabrieal.gymtracker.util.enums.Gender

@Serializable
data class Profile(
    var weight: Double? = null,
    var height: Double? = null,
    var age: Int? = null,
    var goal: FitnessGoal? = null,
    var activityLevel: ActivityLevel? = null,
    var gender: Gender? = null
)