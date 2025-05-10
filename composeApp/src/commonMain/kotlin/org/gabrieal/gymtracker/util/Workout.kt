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
            Spacer(modifier = Modifier.height(8.dp))
            when (selectedDays.count { it }) {
                1 -> {
                    DescriptionText(text = "Day 1: Full Body")
                    DescriptionItalicText(text = "Day 2: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 3: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 4: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 5: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 6: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 7: Rest", color = Colors.LightMaroon)
                }
                2 -> {
                    DescriptionText(text = "Day 1: Full Body (Strength Focus)")
                    DescriptionItalicText(text = "Day 2: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 3: Rest", color = Colors.LightMaroon)
                    DescriptionText(text = "Day 4: Full Body (Hypertrophy Focus)")
                    DescriptionItalicText(text = "Day 5: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 6: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 7: Rest", color = Colors.LightMaroon)
                }
                3 -> {
                    DescriptionText(text = "Day 1: Push (Chest, Shoulders, Triceps)")
                    DescriptionItalicText(text = "Day 2: Rest", color = Colors.LightMaroon)
                    DescriptionText(text = "Day 3: Pull (Back, Biceps)")
                    DescriptionItalicText(text = "Day 4: Rest", color = Colors.LightMaroon)
                    DescriptionText(text = "Day 5: Legs")
                    DescriptionItalicText(text = "Day 6: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 7: Rest", color = Colors.LightMaroon)
                }
                4 -> {
                    DescriptionText(text = "Day 1: Push (Chest Focus, Shoulders, Triceps)")
                    DescriptionText(text = "Day 2: Pull (Back Focus, Biceps)")
                    DescriptionText(text = "Day 3: Legs")
                    DescriptionItalicText(text = "Day 4: Rest", color = Colors.LightMaroon)
                    DescriptionText(text = "Day 5: Arms (Shoulders, Triceps, Biceps)")
                    DescriptionItalicText(text = "Day 6: Rest", color = Colors.LightMaroon)
                    DescriptionItalicText(text = "Day 7: Rest", color = Colors.LightMaroon)
                }
                5 -> {
                    DescriptionText(text = "Day 1: Push (Chest, Shoulders, Triceps)")
                    DescriptionText(text = "Day 2: Pull (Back, Biceps)")
                    DescriptionText(text = "Day 3: Legs")
                    DescriptionItalicText(text = "Day 4: Rest", color = Colors.LightMaroon)
                    DescriptionText(text = "Day 5: Push (Chest, Shoulders, Triceps)")
                    DescriptionText(text = "Day 6: Pull (Back, Biceps)")
                    DescriptionItalicText(text = "Day 7: Rest", color = Colors.LightMaroon)
                }
            }
        }
    }
}