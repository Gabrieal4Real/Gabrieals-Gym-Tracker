package org.gabrieal.gymtracker.data

data class SelectedExercise (
    var name: String? = null,
    var sets: Int? = null,
    var reps:  Pair<Int, Int>? = null
)

data class SelectedExerciseList (
    var day: String? = null,
    var exercises : List<SelectedExercise>? = null,
)