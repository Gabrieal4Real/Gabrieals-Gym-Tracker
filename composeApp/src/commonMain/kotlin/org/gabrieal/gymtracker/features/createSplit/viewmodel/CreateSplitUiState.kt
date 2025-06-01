package org.gabrieal.gymtracker.features.createSplit.viewmodel

import org.gabrieal.gymtracker.model.SelectedExerciseList

data class CreateSplitUiState(
    val selectedDays: List<Boolean> = listOf(true, false, true, false, true, false, false),
    val showImage: Boolean = true,
    val isEditMode: Boolean = false,
    val selectedRoutineList: List<SelectedExerciseList> = emptyList()
)