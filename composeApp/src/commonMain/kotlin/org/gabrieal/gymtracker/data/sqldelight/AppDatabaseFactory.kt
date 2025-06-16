package org.gabrieal.gymtracker.data.sqldelight

import app.cash.sqldelight.ColumnAdapter
import db.CurrentlyActiveRoutineEntity
import db.GymTrackerDatabase
import db.ProfileEntity
import db.SelectedExerciseListEntity
import db.WorkoutHistoryEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.WorkoutProgress
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal
import org.gabrieal.gymtracker.util.enums.Gender
import org.gabrieal.gymtracker.util.systemUtil.createDriver

inline fun <reified T> listColumnAdapter(serializer: KSerializer<T>): ColumnAdapter<List<T>, String> {
    return object : ColumnAdapter<List<T>, String> {
        override fun decode(databaseValue: String): List<T> {
            if (databaseValue.isNotEmpty())
                return Json.decodeFromString(ListSerializer(serializer), databaseValue)
            return emptyList()
        }

        override fun encode(value: List<T>): String =
            Json.encodeToString(ListSerializer(serializer), value)
    }
}

inline fun <reified T : Enum<T>> enumColumnAdapter(): ColumnAdapter<T, String> {
    return object : ColumnAdapter<T, String> {
        override fun decode(databaseValue: String): T =
            enumValues<T>().first { it.name == databaseValue }

        override fun encode(value: T): String = value.name
    }
}

inline fun <reified T : Any> objectColumnAdapter(serializer: KSerializer<T>): ColumnAdapter<T, String> {
    return object : ColumnAdapter<T, String> {
        override fun decode(databaseValue: String): T {
            return if (databaseValue.isNotEmpty()) {
                Json.decodeFromString(serializer, databaseValue)
            } else {
                throw IllegalStateException("Cannot decode empty string to object")
            }
        }

        override fun encode(value: T): String = Json.encodeToString(serializer, value)
    }
}

fun createDatabase(): GymTrackerDatabase =
    GymTrackerDatabase(
        driver = createDriver(),
        profileEntityAdapter = ProfileEntity.Adapter(
            goalAdapter = enumColumnAdapter<FitnessGoal>(),
            activityLevelAdapter = enumColumnAdapter<ActivityLevel>(),
            genderAdapter = enumColumnAdapter<Gender>(),
        ),
        selectedExerciseListEntityAdapter = SelectedExerciseListEntity.Adapter(
            exercisesAdapter = listColumnAdapter(SelectedExercise.serializer())
        ),
        currentlyActiveRoutineEntityAdapter = CurrentlyActiveRoutineEntity.Adapter(
            exercisesAdapter = listColumnAdapter(SelectedExercise.serializer()),
            workoutProgressAdapter = objectColumnAdapter(WorkoutProgress.serializer())
        ),
        workoutHistoryEntityAdapter = WorkoutHistoryEntity.Adapter(
            exercisesAdapter = listColumnAdapter(SelectedExercise.serializer()),
            workoutProgressAdapter = objectColumnAdapter(WorkoutProgress.serializer())
        )
    )