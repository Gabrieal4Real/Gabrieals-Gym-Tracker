package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutProgress(
    val exerciseWeights: List<String> = mutableListOf(),
    val exerciseReps: List<List<String>> = mutableListOf(),
    val exerciseSets: List<List<Boolean>> = mutableListOf(),
)