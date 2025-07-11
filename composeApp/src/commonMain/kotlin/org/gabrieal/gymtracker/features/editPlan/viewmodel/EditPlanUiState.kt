package org.gabrieal.gymtracker.features.editPlan.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExercise

data class EditPlanUiState(
    val day: String = "",
    val exercises: List<SelectedExercise> = emptyList(),
    val showImage: Boolean = true,
    val showRemoveDialog: Boolean = false,
    val currentClickedPosition: Int = 0,
    val placeHolderList: List<String> = emptyList(),
    val isEditMode: Boolean = false
)
