package org.gabrieal.gymtracker.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.ui.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.TinyItalicText

class Workout {
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

    // Enum for exercise tiers
    enum class ExerciseTier(val displayName: String) {
        SPlus("S+ Tier"),
        S("S Tier"),
        APlus("A+ Tier"),
        A("A Tier")
    }

    companion object {
        val days = listOf("M", "T", "W", "T", "F", "S", "S")
        val fullDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val plans = listOf(
            listOf("Full Body"),
            listOf("Full Body (Strength Focus)", "Rest", "Full Body (Hypertrophy Focus)"),
            listOf("Push (Chest, Shoulders, Triceps)", "Rest", "Pull (Back, Biceps)", "Rest", "Legs"),
            listOf("Push (Chest Focus, Shoulders, Triceps)", "Pull (Back Focus, Biceps)", "Legs", "Rest", "Arms (Shoulders, Triceps, Biceps)"),
            listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs", "Rest", "Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)")
        )

        fun selectDays(sliderValue: Float): List<Boolean> {
            val activeIndices = listOf(
                listOf(0),
                listOf(0, 3),
                listOf(0, 2, 4),
                listOf(0, 1, 2, 4),
                listOf(0, 1, 2, 4, 5)
            )

            return List(7) { index ->
                sliderValue.toInt().takeIf { it in 1..5 }
                    ?.let { index in activeIndices[it - 1] } ?: false
            }
        }

        @Composable
        fun WorkoutSplit(selectedDays: List<Boolean>) {
            TinyItalicText(
                text = Resources.strings.xDayWorkoutxDayRest(selectedDays.count { it }, selectedDays.count { !it }),
            )
            Spacer(modifier = Modifier.height(8.dp))
            WorkoutPlan(selectedDays)
        }

        @Composable
        fun WorkoutPlan(selectedDays: List<Boolean>) {
            val workoutPlan = plans.getOrNull(selectedDays.count { it } - 1) ?: emptyList()

            workoutPlan.forEachIndexed { index, workout ->
                if (workout != "Rest") {
                    DescriptionText(text = "Day ${index + 1}: $workout")
                } else {
                    DescriptionItalicText(text = "Day ${index + 1}: Rest", color = Colors.LightMaroon)
                }
            }

            // Fill remaining days with rest
            repeat(7 - workoutPlan.size) {
                DescriptionItalicText(text = "Day ${it + workoutPlan.size + 1}: Rest", color = Colors.LightMaroon)
            }
        }
    }
}