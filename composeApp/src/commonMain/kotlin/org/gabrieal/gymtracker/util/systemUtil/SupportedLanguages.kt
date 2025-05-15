package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf


expect val language: String

enum class Locales(val value: String) {
    MALAY("ms"), ENGLISH("en"), TAMIL("ta")
}

object StringFactory {
    fun createStrings(language: String): StringResources = when (language) {
        Locales.MALAY.value -> MalayStringResources()
        Locales.ENGLISH.value -> EnglishStringResources()
        Locales.TAMIL.value -> TamilStringResources()
        else -> EnglishStringResources()
    }
}


val LocalStringResources: ProvidableCompositionLocal<StringResources> =
    staticCompositionLocalOf {
        EnglishStringResources()
    }

object Resources {
    val strings: StringResources
        @Composable
        @ReadOnlyComposable
        get() = LocalStringResources.current
}

interface StringResources {
    val appName: String
    val nothingHereYet: String
    val startTrackingWorkout: String
    val createSplit: String
    val howManyDays: String
    val recommendedSplit: String
    fun xAmountOfSplit(split: String): String
    val redsIndicateRest: String
    val makeAPlan: String
    val heresHowItWorks: String
    val letsPlanIt: String
    fun xDayWorkoutxDayRest(workout: Int, rest: Int): String
    val notEditedYet: String
    val restDay: String
    val edited: String
}

class EnglishStringResources : StringResources {
    override val appName: String = "Gabrieal's Gym Tracker"
    override val nothingHereYet: String = "It looks empty here"
    override val startTrackingWorkout: String = "Let's start tracking your workouts"
    override val createSplit: String = "Create split"
    override val howManyDays: String = "How many days are you planning to workout?"
    override val recommendedSplit: String = "Based on your selected days, here's a recommended split just for you:"
    override fun xAmountOfSplit(split: String) = "$split-day split"
    override val redsIndicateRest: String = "Reds indicate rest days"
    override val makeAPlan: String = "Make a plan"
    override val heresHowItWorks: String = "Here's how it works:"
    override val letsPlanIt: String = "Let's Go Plan It!"
    override fun xDayWorkoutxDayRest(workout: Int, rest: Int) = "$workout-day workout, $rest-day rest"
    override val notEditedYet: String = "Not edited yet"
    override val restDay: String = "Rest day"
    override val edited: String = "Edited"
}

class MalayStringResources : StringResources {
    override val appName: String = "Gabrieal's Gym Tracker"
    override val nothingHereYet: String = "Nampak kosong je"
    override val startTrackingWorkout: String = "Marilah kita mula memantau latihan >"
    override val createSplit: String = "Cipta split"
    override val howManyDays: String = "Berapa hari anda workout minggu ini?"
    override val recommendedSplit: String = "Berdasarkan hari yang dipilih, ini adalah split yang sesuai untuk anda:"
    override fun xAmountOfSplit(split: String) = "Split $split-hari"
    override val redsIndicateRest: String = "Merah menunjukkan hari rehat"
    override val makeAPlan: String = "Buat plan"
    override val heresHowItWorks: String = "Inilah cara kerja:"
    override val letsPlanIt: String = "Mari Kita Plan It!"
    override fun xDayWorkoutxDayRest(workout: Int, rest: Int) = "$workout-hari latihan, $rest-hari istirahat"
    override val notEditedYet: String = "Belum diubah"
    override val restDay: String = "Hari rehat"
    override val edited: String = "Diubah"
}

class TamilStringResources : StringResources {
    override val appName: String = "Gabrieal's Gym Tracker"
    override val nothingHereYet: String = "இங்கே இன்னும் எதுவும் இல்லை"
    override val startTrackingWorkout: String = "உங்கள் பயிற்சிகளை கண்காணிக்க தொடங்குங்கள் >"
    override val createSplit: String = "பிரிப்பு உருவாக்கவும்"
    override val howManyDays: String = "நீங்கள் எத்தனை நாட்கள் பயிற்சி செய்ய திட்டமிட்டிருக்கிறீர்கள்?"
    override val recommendedSplit: String = "உங்கள் தேர்ந்தெடுத்த நாட்களுக்கு ஏற்ப, இங்கே உங்கள் சிறப்பு பிரிப்பு:"
    override fun xAmountOfSplit(split: String) = "$split-நாள் பிரிப்பு"
    override val redsIndicateRest: String = "சிகப்புகள் ஓய்வு நாட்களை குறிக்கின்றன"
    override val makeAPlan: String = "பிரிப்பு உருவாக்கவும்"
    override val heresHowItWorks: String = "இங்கே எதுவும் செய்யப்படுகிறது:"
    override val letsPlanIt: String = "பிரிப்பு உருவாக்கவும்!"
    override fun xDayWorkoutxDayRest(workout: Int, rest: Int) = "$workout-நாள் பிரிப்பு, $rest-நாள் ஓய்வு"
    override val notEditedYet: String = "முதல் மாற்றப்பட்டது"
    override val restDay: String = "ஓய்வு நாட்கள்"
    override val edited: String = "மாற்றப்பட்டது"
}