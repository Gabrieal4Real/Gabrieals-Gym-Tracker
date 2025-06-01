package org.gabrieal.gymtracker.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Exercise(
    val name: String,
    val targetMuscle: String,
    val secondaryMuscles: List<String>,
    val tier: Int
)

fun decodeExercises(exercise: String): MutableList<Exercise> {
    val exerciseList: MutableList<Exercise> = mutableListOf()
    try {
        Json.decodeFromString<List<Exercise>>(exercise.trim()).forEachIndexed { _, it ->
            exerciseList.add(it)
        }
    } catch (e: Exception) {
        println("decodeFromStringError: ${e.message} ")
    }

    return exerciseList
}
