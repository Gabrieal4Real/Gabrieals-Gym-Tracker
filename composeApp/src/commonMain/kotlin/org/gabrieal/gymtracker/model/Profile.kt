package org.gabrieal.gymtracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    var weight: Double? = null,
    var height: Double? = null,
    var age: Int? = null,
    var goal: FitnessGoal? = null,
    var activityLevel: ActivityLevel? = null,
    var gender: Gender? = null
)