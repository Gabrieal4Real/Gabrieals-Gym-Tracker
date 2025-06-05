package org.gabrieal.gymtracker.util.systemUtil

import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExerciseList

expect fun providePreferences(): SharedPreferences

inline fun <reified T> saveToPreferences(key: String, value: T) {
    val json = Json.encodeToString(value)
    providePreferences().putString(key, json)
}

inline fun <reified T> loadFromPreferences(key: String, default: T): T {
    return try {
        val json = providePreferences().getString(key)
        Json.decodeFromString(json)
    } catch (ex: Exception) {
        println("decodeFromStringError for key \"$key\": ${ex.message}")
        default
    }
}

interface SharedPreferences {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String = ""): String
    fun putInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun clear()
}

fun setSelectedRoutineListToSharedPreferences(list: List<SelectedExerciseList>) {
    saveToPreferences("selectedRoutineList", list)
}

fun getSelectedRoutineListFromSharedPreferences(): MutableList<SelectedExerciseList> {
    return loadFromPreferences("selectedRoutineList", mutableListOf())
}

fun setProfileToSharedPreferences(profile: Profile) {
    saveToPreferences("profile", profile)
}

fun getProfileFromSharedPreferences(): Profile {
    return loadFromPreferences("profile", Profile())
}