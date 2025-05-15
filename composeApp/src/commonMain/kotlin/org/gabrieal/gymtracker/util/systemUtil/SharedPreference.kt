package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.data.SelectedExerciseList

interface SharedPreferences {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String = ""): String
    fun putInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun clear()
}

expect fun providePreferences(context: Any?): SharedPreferences

@Composable
fun getSelectedRoutineList(): MutableList<SelectedExerciseList> {
    var selectedRoutineList by remember { mutableStateOf(mutableListOf<SelectedExerciseList>()) }

    getCurrentContext().let {
        try {
            selectedRoutineList = Json.decodeFromString<MutableList<SelectedExerciseList>>(providePreferences(it).getString("selectedRoutineList"))
            println("selectedRoutineList: $selectedRoutineList")
        } catch (ex: Exception) {
            println("decodeFromStringError: ${ex.message} ")
        }
    }

    return selectedRoutineList
}