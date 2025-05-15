package org.gabrieal.gymtracker.data

import kotlinx.serialization.Serializable

@Serializable
data class SelectedExercise (
    var name: String? = null,
    var sets: Int? = null,
    var reps:  Pair<Int, Int>? = null
)

@Serializable
data class SelectedExerciseList (
    var position : Int? = null,
    var day: String? = null,
    var exercises : List<SelectedExercise>? = null,
)