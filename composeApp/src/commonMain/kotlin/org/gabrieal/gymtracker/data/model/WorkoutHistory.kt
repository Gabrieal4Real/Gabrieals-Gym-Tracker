package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutHistory(
    val id: Long,
    val startedOn: String?,
    val finishedDate: String,
    val routineName: String?,
    val startingDate: String?,
    val exercises: List<SelectedExercise>?,
    val workoutProgress: WorkoutProgress?,
    val completedVolume: Double?
)
