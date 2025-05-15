package org.gabrieal.gymtracker.util.appUtil

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.views.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.views.widgets.DescriptionText
import org.gabrieal.gymtracker.views.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.systemUtil.Resources

class Workout {
    companion object {
        val days = listOf("M", "T", "W", "T", "F", "S", "S")
        val fullDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val planTitles = listOf("Full Body", "Push", "Pull", "Leg", "Accessory")

        private val plans = listOf(
            listOf("Full Body"),
            listOf("Full Body (Strength Focus)", "Full Body (Hypertrophy Focus)"),
            listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs"),
            listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs", "Accessory (Target muscle of your choosing)"),
            listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs", "Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)")
        )

        val repRanges = listOf(
            1 to 5,
            6 to 10,
            8 to 12,
            12 to 15,
            15 to 20
        )

        fun getCurrentPlan(selectedDays: List<Boolean>): List<String> {
            val currentPlan = mutableListOf<String>()
            var position = 0
            selectedDays.forEachIndexed { index, isSelected ->
                if (isSelected) {
                    currentPlan.add(plans[selectedDays.count { it } - 1][position])
                    position += 1
                } else {
                    currentPlan.add("")
                }
            }

            return currentPlan
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
            var position = 0
            selectedDays.forEachIndexed { index, isSelected ->
                if (isSelected) {
                    DescriptionText("Day ${index + 1}: ${plans[selectedDays.count { it } - 1][position]}")
                    position += 1
                } else {
                    DescriptionItalicText("Day ${index + 1}: Rest", color = Colors.LightMaroon)
                }
            }
        }
    }
}