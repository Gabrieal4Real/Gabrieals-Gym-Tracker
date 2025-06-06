package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.util.app.longFormDays

@Serializable
data class ConvertedTemplate(
    val `1_day`: List<List<SelectedExerciseList>> = emptyList(),
    val `2_day`: List<List<SelectedExerciseList>> = emptyList(),
    val `3_day`: List<List<SelectedExerciseList>> = emptyList(),
    val `4_day`: List<List<SelectedExerciseList>> = emptyList(),
    val `5_day`: List<List<SelectedExerciseList>> = emptyList(),
)

@Serializable
data class Template(
    val `1_day`: List<List<TemplateExerciseList>> = emptyList(),
    val `2_day`: List<List<TemplateExerciseList>> = emptyList(),
    val `3_day`: List<List<TemplateExerciseList>> = emptyList(),
    val `4_day`: List<List<TemplateExerciseList>> = emptyList(),
    val `5_day`: List<List<TemplateExerciseList>> = emptyList(),
)

@Serializable
data class TemplateExerciseList(
    val day: String,
    val exercises: List<TemplateExercise>,
    val position: Int
)

@Serializable
data class TemplateExercise(
    val name: String,
    val reps: List<Int>,
    val sets: Int
)

private val json = Json { ignoreUnknownKeys = true }

fun decodeTemplate(template: String): ConvertedTemplate {
    var tempTemplate = Template()
    try {
        tempTemplate = json.decodeFromString<Template>(template.trim())
    } catch (e: Exception) {
        println("decodeFromStringError: ${e.message} ")
    }

    return convertTemplateToConvertedTemplate(tempTemplate)
}

fun convertTemplateToConvertedTemplate(template: Template): ConvertedTemplate {
    fun convertDay(dayList: List<List<TemplateExerciseList>>): List<List<SelectedExerciseList>> {
        return dayList.map { innerList ->
            innerList.map { templateList ->
                SelectedExerciseList(
                    position = longFormDays.indexOf(templateList.day),
                    day = templateList.day,
                    exercises = templateList.exercises.map { exercise ->
                        SelectedExercise(
                            name = exercise.name,
                            sets = exercise.sets,
                            reps = exercise.reps.takeIf { it.size == 2 }?.let { Pair(it[0], it[1]) }
                        )
                    }
                )
            }
        }
    }

    return ConvertedTemplate(
        `1_day` = convertDay(template.`1_day`),
        `2_day` = convertDay(template.`2_day`),
        `3_day` = convertDay(template.`3_day`),
        `4_day` = convertDay(template.`4_day`),
        `5_day` = convertDay(template.`5_day`)
    )
}
