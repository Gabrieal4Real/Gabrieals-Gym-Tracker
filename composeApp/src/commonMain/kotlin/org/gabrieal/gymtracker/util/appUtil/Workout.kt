package org.gabrieal.gymtracker.util.appUtil

val shortFormDays = listOf("M", "T", "W", "T", "F", "S", "S")
val longFormDays =
    listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
val planTitles = listOf("Full Body", "Push", "Pull", "Leg", "Accessory")
val plans = listOf(
    listOf("Full Body"),
    listOf("Full Body (Strength Focus)", "Full Body (Hypertrophy Focus)"),
    listOf("Push (Chest, Shoulders, Triceps)", "Pull (Back, Biceps)", "Legs"),
    listOf(
        "Push (Chest, Shoulders, Triceps)",
        "Pull (Back, Biceps)",
        "Legs",
        "Accessory (Target muscle of your choosing)"
    ),
    listOf(
        "Push (Chest, Shoulders, Triceps)",
        "Pull (Back, Biceps)",
        "Legs",
        "Push (Chest, Shoulders, Triceps)",
        "Pull (Back, Biceps)"
    )
)
val repRanges = listOf(1 to 5, 6 to 10, 8 to 12, 12 to 15, 15 to 20)

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