package org.gabrieal.gymtracker.util.appUtil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.model.enums.bmiSegments
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.widgets.DescriptionText
import org.gabrieal.gymtracker.views.widgets.SubtitleText
import org.gabrieal.gymtracker.views.widgets.TinyText
import kotlin.math.round
import kotlin.math.roundToInt

val shortFormDays = listOf("M", "T", "W", "T", "F", "S", "S")
val longFormDays =
    listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
val planTitles =
    listOf("Full Body", "Push", "Pull", "Legs", "Accessory", "Upper Body", "Lower Body")
val plans = listOf(
    listOf("Full Body"),
    listOf("Upper Body", "Lower Body"),
    listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs"),
    listOf(
        "Push (Chest, Shoulders, Triceps)",
        "Pull (Back, Biceps)",
        "Legs",
        "Accessory (Target muscle of your choosing)"
    ),
    listOf(
        "Upper Body",
        "Lower Body",
        "Push (Chest, Shoulders, Triceps)",
        "Pull (Back, Biceps)",
        "Legs",
    )
)

val repRanges = listOf(1 to 5, 6 to 10, 8 to 12, 12 to 15, 15 to 20)
val timerRangesInSeconds = listOf(300, 180, 120, 90, 60)

// KG to LBS
val matchedDumbbellWeights = listOf(
    1.0 to 2,
    1.5 to 3,
    2.0 to 5,
    3.0 to 8,
    4.0 to 10,
    5.0 to 12,
    6.0 to 15,
    7.0 to 17.5,
    8.0 to 20,
    9.0 to 22.5,
    10.0 to 25,
    12.5 to 30,
    15.0 to 35,
    17.5 to 40,
    20.0 to 45,
    22.5 to 50,
    25.0 to 55,
    27.5 to 60,
    30.0 to 65,
    32.5 to 70,
    35.0 to 75,
    37.5 to 80,
    40.0 to 85,
    42.5 to 90,
    45.0 to 95,
    47.5 to 100
)

val matchedPlateWeights = listOf(
    1.25 to 2.5,
    2.5 to 5,
    5.0 to 10,
    10.0 to 25,
    15.0 to 35,
    20.0 to 45,
    25.0 to 55
)

val matchedMachineWeights = listOf(
    5.0 to 10,
    10.0 to 20,
    15.0 to 30,
    20.0 to 40,
    25.0 to 50,
    30.0 to 60,
    35.0 to 70,
    40.0 to 80,
    45.0 to 90,
    50.0 to 100,
    55.0 to 110,
    60.0 to 120,
    65.0 to 130,
    70.0 to 140,
    75.0 to 150,
    80.0 to 160,
    85.0 to 170,
    90.0 to 180,
    95.0 to 190,
    100.0 to 200
)

fun getCurrentPlan(selectedDays: List<Boolean>): List<String> {
    val currentPlan = mutableListOf<String>()
    var position = 0
    selectedDays.forEach {
        if (it) {
            currentPlan.add(plans[selectedDays.count { it } - 1][position])
            position += 1
        } else {
            currentPlan.add("")
        }
    }

    return currentPlan
}

fun getCurrentTimerInSeconds(repRange: Pair<Int, Int>?): Int {
    val repRangesIndex = repRanges.indexOf(repRange)
    return timerRangesInSeconds.getOrNull(repRangesIndex) ?: 90
}

fun formatRestTime(seconds: Int): String {
    return when {
        seconds <= 90 -> {
            "${seconds}s"
        }

        seconds < 3600 -> {
            val minutes = seconds / 60
            "${minutes}m"
        }

        else -> {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            if (minutes == 0) {
                "${hours}h"
            } else {
                "${hours}h ${minutes}m"
            }
        }
    }
}

fun getPlanTitle(routineName: String?): String {
    var planTitle = routineName
    if (routineName.equals("Legs")) {
        planTitle = "Leg"
    }

    return planTitle ?: "Rest"
}

@Composable
fun getBMISummary(weight: Double?, height: Double?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (weight == null || height == null) {
            SubtitleText("Body Mass Index Summary")
            Spacer(modifier = Modifier.height(2.dp))
            TinyText("Your BMI will be calculated based on your height and weight")
            return
        }


        val heightInMeters = height / 100
        val bmi = weight / (heightInMeters * heightInMeters)
        val roundedBMI = (round(bmi * 100) / 100)

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

fun getListForWeightHeightSpinner(weightHeightBMIClicked: Int): List<String> {
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
    }
    return list
}

@Composable
fun BMIBarChart(roundedBMI: Double) {
    val segmentInfo = bmiSegments.map {
        Pair(it.range, it.color)
    }

    val progress = ((roundedBMI.toFloat() - 13f) / (41f - 13f)).coerceIn(0.01f, 0.99f)
    val selectedSegment = bmiSegments.find { it.maxValue >= roundedBMI }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubtitleText("Body Mass Index Summary")
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        segmentInfo.forEach { segmentInfo ->
                            Box(
                                modifier = Modifier
                                    .weight(segmentInfo.first)
                                    .background(segmentInfo.second)
                                    .height(48.dp)
                            )
                        }
                    }
                }
            }

            Row {
                Spacer(modifier = Modifier.weight(progress))
                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .background(colors.white)
                        .width(3.dp)
                )
                Spacer(modifier = Modifier.weight(1 - progress))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        DescriptionText(
            selectedSegment?.label ?: "Extremely Obese",
            color = selectedSegment?.color ?: colors.lightMaroon
        )
    }
}