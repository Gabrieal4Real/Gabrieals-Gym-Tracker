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
    companion object {
        val days = listOf("M", "T", "W", "T", "F", "S", "S")
        val fullDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val planTitles = listOf("Full Body", "Push", "Pull", "Leg", "Accessory")
        val plans = listOf(
            listOf("Full Body"),
            listOf("Full Body (Strength Focus)", "Rest", "Rest", "Full Body (Hypertrophy Focus)"),
            listOf("Push (Chest, Shoulders, Triceps)", "Rest", "Pull (Back, Biceps)", "Rest", "Legs"),
            listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs", "Rest", "Accessory (Target muscle of your choosing)"),
            listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs", "Rest", "Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)")
        )
        val repRanges = listOf(
            1 to 5,
            6 to 10,
            8 to 12,
            12 to 15,
            15 to 20
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
                Resources.strings.xDayWorkoutxDayRest(selectedDays.count { it }, selectedDays.count { !it }),
            )
            Spacer(modifier = Modifier.height(8.dp))
            WorkoutPlan(selectedDays)
        }

        @Composable
        fun WorkoutPlan(selectedDays: List<Boolean>) {
            val workoutPlan = plans.getOrNull(selectedDays.count { it } - 1) ?: emptyList()

            workoutPlan.forEachIndexed { index, workout ->
                if (workout != "Rest") {
                    DescriptionText("Day ${index + 1}: $workout")
                } else {
                    DescriptionItalicText("Day ${index + 1}: Rest", color = Colors.LightMaroon)
                }
            }

            // Fill remaining days with rest
            repeat(7 - workoutPlan.size) {
                DescriptionItalicText("Day ${it + workoutPlan.size + 1}: Rest", color = Colors.LightMaroon)
            }
        }
    }
}