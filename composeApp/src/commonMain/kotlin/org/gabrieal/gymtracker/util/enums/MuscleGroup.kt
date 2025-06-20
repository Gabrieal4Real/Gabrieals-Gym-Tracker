package org.gabrieal.gymtracker.util.enums

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
    Calves("Calves"),
    Triceps("Triceps");

    companion object {
        fun relatedMuscles(selected: String): List<String> {
            return when (selected) {
                FrontDelt.displayName -> listOf(FrontDelt.displayName)
                MiddleDelt.displayName -> listOf(MiddleDelt.displayName)
                RearDelt.displayName -> listOf(RearDelt.displayName)
                Shoulders.displayName -> listOf(
                    RearDelt.displayName,
                    FrontDelt.displayName,
                    MiddleDelt.displayName
                )

                Biceps.displayName -> listOf(Biceps.displayName)
                Triceps.displayName -> listOf(Triceps.displayName)
                Arms.displayName -> listOf(Triceps.displayName, Biceps.displayName)

                Chest.displayName -> listOf(Chest.displayName)
                Abs.displayName -> listOf(Abs.displayName)

                Traps.displayName -> listOf(Traps.displayName)
                Back.displayName -> listOf(
                    Back.displayName,
                    Traps.displayName,
                    RearDelt.displayName
                )

                Calves.displayName -> listOf(Calves.displayName)
                Glutes.displayName -> listOf(Glutes.displayName)
                Hamstrings.displayName -> listOf(Hamstrings.displayName)
                Quads.displayName -> listOf(Quads.displayName)
                Legs.displayName -> listOf(
                    Calves.displayName,
                    Quads.displayName,
                    Hamstrings.displayName,
                    Glutes.displayName
                )

                else -> listOf()
            }
        }
    }
}
