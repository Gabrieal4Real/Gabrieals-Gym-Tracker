package org.gabrieal.gymtracker.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class Routine {
    // Enum for muscle groups
    enum class MuscleGroup(val displayName: String) {
        Abs("Abs"),
        Arms("Arms"),
        Back("Back"),
        Biceps("Biceps"),
        Chest("Chest"),
        FrontDelt("Front Delt"),
        Glutes("Glutes"),
        Hamstrings("Hamstrings"),
        Legs("Legs"),
        MiddleDelt("Middle Delt"),
        Quads("Quads"),
        RearDelt("Rear Delt"),
        Shoulders("Shoulders"),
        Traps("Traps"),
        Triceps("Triceps")
    }

    // Enum for exercise tiers, with S+ Tier as 0
    enum class ExerciseTier(val displayName: String) {
        SPlus("S+ Tier"),
        S("S Tier"),
        APlus("A+ Tier"),
        A("A Tier");

        companion object {
            fun fromString(tier: Int): ExerciseTier {
                return when (tier) {
                    0 -> SPlus
                    1 -> S
                    2 -> APlus
                    3 -> A
                    else -> A
                }
            }
        }
    }
}

@Serializable
data class Exercise(
    val name: String,
    val muscleGroup: List<String>,
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
