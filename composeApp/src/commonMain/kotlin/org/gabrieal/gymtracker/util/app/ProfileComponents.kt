package org.gabrieal.gymtracker.util.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.data.model.ProteinInput
import org.gabrieal.gymtracker.data.model.BMISegment
import org.gabrieal.gymtracker.data.model.CalorieGoalResult
import org.gabrieal.gymtracker.data.model.CalorieInput
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal
import org.gabrieal.gymtracker.util.enums.Gender
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyText
import kotlin.math.round
import kotlin.math.roundToInt

val bmiSegments = listOf(
    BMISegment(3f, Color(0xFFBE3E33), "Underweight (Severely)", 16f),  // 13 - 16
    BMISegment(1f, Color(0xFFD07560), "Underweight (Moderate)", 17f),  // 16 - 17
    BMISegment(1.5f, Color(0xFFF1BC6A), "Underweight (Mild)", 18.5f),  // 17 - 18.5
    BMISegment(7f, Color(0xFF8DC367), "Normal", 25.5f),                // 18.5 - 25.5
    BMISegment(4.5f, Color(0xFFF1BC6A), "Overweight", 30f),            // 25.5 - 30
    BMISegment(5f, Color(0xFFE2AD8D), "Obese Class I", 35f),           // 30 - 35
    BMISegment(5f, Color(0xFFD07560), "Obese Class II", 40f),          // 35 - 40
    BMISegment(1f, Color(0xFFBE3E33), "Obese Class III", 41f)          // 40 - 41
)

@Composable
fun getBMISummary(weight: Double?, height: Double?) {
    if (weight == null || height == null) {
        return
    }

    CustomCard(
        enabled = true,
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                SubtitleText("Body Mass Index Summary")

                val heightInMeters = height / 100
                val bmi = weight / (heightInMeters * heightInMeters)
                val roundedBMI = (round(bmi * 100) / 100)

                Spacer(modifier = Modifier.height(8.dp))
                BMIBarChart(roundedBMI)
                Spacer(modifier = Modifier.height(8.dp))

                val minHealthyBMI = 18.5
                val maxHealthyBMI = 25.0

                val minHealthyWeight = minHealthyBMI * heightInMeters * heightInMeters
                val maxHealthyWeight = maxHealthyBMI * heightInMeters * heightInMeters

                val roundedMinWeight = (round(minHealthyWeight * 10) / 10)
                val roundedMaxWeight = maxHealthyWeight.roundToInt()

                val weightToLose = if (weight > maxHealthyWeight) {
                    round((weight - maxHealthyWeight) * 10) / 10
                } else null

                val weightToGain = if (weight < minHealthyWeight) {
                    round((minHealthyWeight - weight) * 10) / 10
                } else null

                val listOfBMIs = mutableListOf(
                    Pair("Your BMI: ", "$roundedBMI kg/m²"),
                    Pair("Healthy BMI range: ", "18.5 kg/m² - 25.0 kg/m²"),
                    Pair("Healthy weight for your height: ", "$roundedMinWeight kg - $roundedMaxWeight kg"),
                )

                weightToLose?.let { listOfBMIs.add(Pair("Lose $it kg to reach a BMI of 25.0 kg/m²", "")) }
                weightToGain?.let { listOfBMIs.add(Pair("Gain $it kg to reach a BMI of 18.5 kg/m²", "")) }

                listOfBMIs.forEach {
                    TinyText("${it.first}${it.second}", modifier = Modifier.fillMaxWidth())
                }
            }
        }
    )

    Spacer(modifier = Modifier.height(8.dp))
}

fun getListForWeightHeightAgeSpinner(weightHeightBMIClicked: Int): List<String> {
    val list = mutableListOf<String>()
    when (weightHeightBMIClicked) {
        0 -> {
            for (i in 0..250) {
                list.add("$i KG")
            }
        }

        1 -> {
            for (i in 0..250) {
                list.add("$i CM")
            }
        }

        2 -> {
            for (i in 5..150) {
                list.add("$i")
            }
        }
    }
    return list
}

@Composable
fun BMIBarChart(roundedBMI: Double) {
    val progress = ((roundedBMI.toFloat() - 13f) / (41f - 13f)).coerceIn(0.01f, 0.99f)
    val scale by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800), label = ""
    )

    val selectedSegment = bmiSegments.find { it.maxValue >= roundedBMI } ?: bmiSegments.last()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background Bar
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(8.dp))
                ) {
                    bmiSegments.forEach {
                        Box(
                            modifier = Modifier
                                .weight(it.range)
                                .height(48.dp)
                                .background(it.color)
                        )
                    }
                }
            }

            // Marker
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(scale))
                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .width(3.dp)
                        .background(colors.white)
                        .shadow(8.dp, RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.weight(1f - scale))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionText(
            text = selectedSegment.label,
            color = selectedSegment.color
        )
    }
}

fun calculateProteinGrams(input: ProteinInput): Int {
    val multiplier = when (input.goal) {
        FitnessGoal.MAINTENANCE -> when (input.activityLevel) {
            ActivityLevel.SEDENTARY -> 1.0
            ActivityLevel.LIGHTLY_ACTIVE -> 1.2
            ActivityLevel.MODERATELY_ACTIVE -> 1.4
            ActivityLevel.VERY_ACTIVE, ActivityLevel.SUPER_ACTIVE -> 1.6
        }
        FitnessGoal.FAT_LOSS -> 1.8
        FitnessGoal.MUSCLE_GAIN -> 2.0
    }
    return (input.weightKg * multiplier).roundToInt()
}

fun calculateBMR(input: CalorieInput): Double {
    return when (input.gender) {
        Gender.MALE -> (10 * input.weightKg) + (6.25 * input.heightCm) - (5 * input.age) + 5
        Gender.FEMALE -> (10 * input.weightKg) + (6.25 * input.heightCm) - (5 * input.age) - 161
    }
}

fun calculateMaintenanceCalories(input: CalorieInput): Double {
    val bmr = calculateBMR(input)
    return bmr * input.activityLevel.multiplier
}

fun generateGoalBreakdown(input: CalorieInput): List<CalorieGoalResult> {
    val maintenance = calculateMaintenanceCalories(input)

    val goals = listOf(
        Triple("Extreme weight loss", -1.0, -1000),
        Triple("Weight loss", -0.5, -500),
        Triple("Mild weight loss", -0.25, -250),
        Triple("Maintain weight", 0.0, 0),
        Triple("Mild weight gain", 0.25, 250),
        Triple("Weight gain", 0.5, 500),
        Triple("Fast weight gain", 1.0, 1000),
    )

    return goals.map { (label, weightChange, calorieDiff) ->
        val calories = (maintenance + calorieDiff).coerceAtLeast(1000.0).toInt()
        val percentage = ((calories / maintenance) * 100).toInt()
        CalorieGoalResult(label, weightChange, calories, percentage)
    }
}