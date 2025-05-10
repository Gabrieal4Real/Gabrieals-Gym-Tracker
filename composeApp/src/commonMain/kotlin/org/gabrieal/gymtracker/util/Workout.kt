package org.gabrieal.gymtracker.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.TinyItalicText

class Workout {
    companion object {

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
        fun getWorkoutSplit(selectedDays: List<Boolean>) {
            TinyItalicText(
                text = "${selectedDays.count { it }}-Day Workout, ${selectedDays.count { !it }}-Day Rest",
            )
            Spacer(modifier = Modifier.height(4.dp))
            when (selectedDays.count { it }) {
                1 -> DescriptionText(text = "Monday: Full Body\n" +
                        "Tuesday: Rest\n" +
                        "Wednesday: Rest\n" +
                        "Thursday: Rest\n" +
                        "Friday: Rest\n" +
                        "Saturday: Rest\n" +
                        "Sunday: Rest")
                2 -> DescriptionText(text = "Monday: Full Body\n" +
                        "Tuesday: Rest\n" +
                        "Wednesday: Rest\n" +
                        "Thursday: Full Body\n" +
                        "Friday: Rest\n" +
                        "Saturday: Rest\n" +
                        "Sunday: Rest")
                3 -> DescriptionText(text = "Monday: Push (Chest, Shoulders, Triceps)\n" +
                        "Tuesday: Rest\n" +
                        "Wednesday: Pull (Back, Biceps)\n" +
                        "Thursday: Rest\n" +
                        "Friday: Legs\n" +
                        "Saturday: Rest\n" +
                        "Sunday: Rest")
                4 -> DescriptionText(text = "Monday: Push (Main Chest, Shoulders, Triceps)\n" +
                        "Tuesday: Rest\n" +
                        "Wednesday: Pull (Main Back, Biceps)\n" +
                        "Thursday: Legs\n" +
                        "Friday: Arms (Shoulders, Triceps, Biceps)\n" +
                        "Saturday: Rest\n" +
                        "Sunday: Rest")
                5 -> DescriptionText(text = "Monday: Push (Chest, Shoulders, Triceps)\n" +
                        "Tuesday: Pull (Back, Biceps)\n" +
                        "Wednesday: Legs\n" +
                        "Thursday: Rest\n" +
                        "Friday: Push (Chest, Shoulders, Triceps)\n" +
                        "Saturday: Pull (Back, Biceps)\n" +
                        "Sunday: Rest")
            }
        }
    }
}