package org.gabrieal.gymtracker.util.systemUtil

import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.model.Profile
import org.gabrieal.gymtracker.model.SelectedExerciseList

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


fun getSelectedRoutineListFromSharedPreferences(context: Any?): MutableList<SelectedExerciseList> {
    var selectedRoutineList = mutableListOf<SelectedExerciseList>()

    try {
        selectedRoutineList = Json.decodeFromString<MutableList<SelectedExerciseList>>(
            providePreferences(context).getString("selectedRoutineList")
        )
    } catch (ex: Exception) {
        println("decodeFromStringError: ${ex.message} ")
    }

    return selectedRoutineList
}


fun getProfileFromSharedPreferences(context: Any?): Profile {
    var profile = Profile()

    try {
        profile = Json.decodeFromString<Profile>(
            providePreferences(context).getString("profile")
        )
    } catch (ex: Exception) {
        println("decodeFromStringError: ${ex.message} ")
    }

    return profile
}